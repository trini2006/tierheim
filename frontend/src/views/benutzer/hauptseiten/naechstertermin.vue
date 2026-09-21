<template>
  <div>
    <!-- Wenn ein Termin vorhanden ist -->
    <div v-if="naechsterTermin">
      <!-- Die salbeigrüne Hundekarte -->
      <div class="bg-[#D3DDD1] p-5 rounded-2xl shadow-sm border border-gray-200 relative">
        <div class="flex gap-4">
          <!-- Hundebild -->
          <div class="w-28 h-28 rounded-xl overflow-hidden bg-gray-300 flex-shrink-0 shadow-inner">
            <img
              :src="naechsterTermin.hund?.bildUrl || 'https://images.unsplash.com/photo-1543466835-00a7907e9de1?w=300&auto=format&fit=crop'"
              :alt="naechsterTermin.hund?.name || 'Hund'"
              class="w-full h-full object-cover"
            />
          </div>

          <!-- Hundedetails -->
          <div class="flex-grow space-y-2 text-left relative">
            <!-- Statuspunkt (Orange bei Erfahrung/Besonderheit oder Grün) -->
            <span :class="['absolute top-0 right-0 w-3 h-3 rounded-full', naechsterTermin.hund?.erfahrung ? 'bg-orange-500' : 'bg-green-500']"></span>

            <h3 class="text-xl font-bold text-gray-900 leading-tight">{{ naechsterTermin.hund?.name || 'Unbekannt' }}</h3>
            <p class="text-xs text-gray-600">
              {{ naechsterTermin.hund?.rasse || 'Mischling' }}<br />
              {{ naechsterTermin.hund?.jahre || 0 }} Jahre, {{ naechsterTermin.hund?.geschlecht || 'Rüde' }}
            </p>

            <!-- Charaktereigenschaften / Labels -->
            <div class="flex flex-wrap gap-1.5 pt-1">
              <span 
                v-for="label in (naechsterTermin.hund?.labels || [{name: 'ruhig'}, {name: 'zurückhaltend'}])" 
                :key="label.id || label.name"
                class="bg-white/80 px-2 py-0.5 rounded-full text-[11px] text-gray-700 font-medium"
              >
                {{ label.name }}
              </span>
            </div>
          </div>
        </div>
      </div>

      <!-- Termindaten -->
      <div class="mt-6 space-y-2 text-left px-2">
        <div class="flex text-sm">
          <span class="font-bold w-20 text-gray-700">Datum:</span>
          <span class="text-gray-900">{{ formatiereDatum(naechsterTermin.datum) }}</span>
        </div>
        <div class="flex text-sm">
          <span class="font-bold w-20 text-gray-700">Uhrzeit:</span>
          <span class="text-gray-900">{{ formatiereUhrzeit(naechsterTermin.von || naechsterTermin.zeitAb) }} - {{ formatiereUhrzeit(naechsterTermin.bis || naechsterTermin.zeitBis) }} Uhr</span>
        </div>
      </div>

      <!-- Stornieren Button -->
      <div class="mt-6 text-left px-2">
        <button
          @click="terminStornieren(naechsterTermin.id)"
          class="bg-red-400 hover:bg-red-500 text-gray-900 font-bold px-6 py-2.5 rounded-full shadow-sm transition-colors text-sm"
        >
          Termin stornieren
        </button>
      </div>
    </div>

    <!-- Fallback, falls keine Reservierung existiert -->
    <div v-else class="text-sm text-gray-500 italic p-4 text-left">
      Aktuell kein nächster Termin geplant.
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const naechsterTermin = ref(null)

onMounted(async () => {
  await ladeErstenTermin()
})

const ladeErstenTermin = async () => {
  try {
    // Mitgliedsnummer aus dem LocalStorage ermitteln
    const storedData = JSON.parse(localStorage.getItem('terminData') || localStorage.getItem('bookingFinal') || '{}')
    const mitgliedId = storedData.mitgliedId || storedData.mitgliedsnummer

    if (!mitgliedId) {
      console.warn('Keine Mitgliedsnummer gefunden.')
      return
    }

    const heute = new Date().toISOString().split('T')[0]
    const inSechsMonaten = new Date(new Date().setMonth(new Date().getMonth() + 6)).toISOString().split('T')[0]

    // Abruf über den Backend-Endpunkt mit der Mitgliedsnummer
    const res = await fetch(`/reservierung/all?von=${heute}&bis=${inSechsMonaten}&mitgliedId=${mitgliedId}`)
    
    if (res.ok) {
      const data = await res.json()
      // Filtere nur aktive Termine heraus und nimm exakt den ersten Eintrag der Liste
      const aktiveTermine = data.filter(t => t.status !== 'STORNIERT')
      if (aktiveTermine.length > 0) {
        naechsterTermin.value = aktiveTermine[0]
      }
    } else {
      console.error('Fehler beim Laden der Reservierungen:', res.status)
    }
  } catch (e) {
    console.error('Netzwerkfehler:', e)
  }
}

// Datum formatieren (z. B. "2026-07-01" -> "Mittwoch, 01.07.2026")
const formatiereDatum = (datumStr) => {
  if (!datumStr) return ''
  try {
    const [jahr, monat, tag] = datumStr.split('-')
    if (!jahr || !monat || !tag) return datumStr
    const dateObj = new Date(jahr, monat - 1, tag)
    const wochentag = dateObj.toLocaleDateString('de-DE', { weekday: 'long' })
    return `${wochentag}, ${tag}.${monat}.${jahr}`
  } catch {
    return datumStr
  }
}

// Uhrzeit formatieren (z. B. "10:00:00" -> "10:00")
const formatiereUhrzeit = (zeitStr) => {
  if (!zeitStr) return ''
  return zeitStr.substring(0, 5)
}

const terminStornieren = async (id) => {
  if (!confirm('Möchten Sie diesen Termin wirklich stornieren?')) return
  
  try {
    const res = await fetch(`/reservierung/cancel/${id}`, {
      method: 'POST'
    })
    if (res.ok) {
      naechsterTermin.value = null
    } else {
      alert('Stornierung fehlgeschlagen.')
    }
  } catch (e) {
    console.error('Fehler bei der Stornierung:', e)
  }
}
</script>