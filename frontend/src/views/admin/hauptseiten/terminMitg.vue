<template>
  <div class="max-w-6xl mx-auto p-4 md:p-6">
    <!-- Haupt-Grid: Links Mitglieder, Rechts Zeitwahl -->
    <div class="grid grid-cols-1 md:grid-cols-2 gap-8 items-start">
      <!-- LINKE SEITE: Mitglieder Auswahl -->
      <div class="space-y-4">
        <h2 class="text-xl font-bold text-gray-800">Wähle ein Mitglied aus...</h2>

        <div class="space-y-3">
          <div
            v-for="mitglied in mitgliederListe"
            :key="mitglied.id"
            @click="waehleMitglied(mitglied)"
            class="flex items-center justify-between p-4 rounded-3xl shadow-sm border cursor-pointer transition-all"
            :class="
              ausgewaehltesMitglied?.id === mitglied.id
                ? 'bg-[#b8c9b4] border-emerald-700 ring-2 ring-emerald-600'
                : 'bg-[#D3DDD1] border-gray-200 hover:bg-[#c2cebf]'
            "
          >
            <div class="flex items-center gap-3">
              <div
                class="w-10 h-10 rounded-full bg-emerald-900 flex items-center justify-center text-white font-bold"
              >
                👤
              </div>
              <span class="font-bold text-gray-800 text-sm md:text-base">{{
                mitglied.mitgliedsnummer
              }}</span>
            </div>
            <span
              class="w-3 h-3 rounded-full"
              :class="mitglied.hatErfahrung ? 'bg-orange-400' : 'bg-green-500'"
            ></span>
          </div>

          <p v-if="mitgliederListe.length === 0" class="text-sm text-gray-500 italic">
            Keine Mitglieder gefunden.
          </p>
        </div>
      </div>
      <!-- RECHTE SEITE -->
      <ZeitAuswahl :deaktiviert="!ausgewaehltesMitglied" @change="auswahl = $event" />
    </div>

    <!-- UNTERER BEREICH: Weiter-Button -->
    <div class="flex justify-end mt-8">
      <button
        @click="weiterZuHundewahl"
        :disabled="!ausgewaehltesMitglied || !auswahlGueltig"
        class="flex items-center gap-3 px-8 py-3 rounded-full shadow-md font-bold transition-all"
        :class="
          !ausgewaehltesMitglied || !auswahlGueltig
            ? 'bg-gray-300 text-gray-500 cursor-not-allowed'
            : 'bg-[#D3DDD1] hover:bg-[#c2cebf] text-gray-800 border border-gray-300'
        "
      >
        <span>Weiter</span>
        <div
          class="w-8 h-8 rounded-full bg-emerald-600 flex items-center justify-center text-white"
        >
          ✓
        </div>
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import ZeitAuswahl from '@/components/utils/ZeitAuswahl.vue'

const router = useRouter()
const mitgliederListe = ref([])
const ausgewaehltesMitglied = ref(null)
const auswahl = ref(null)

function waehleMitglied(mitglied) {
  ausgewaehltesMitglied.value = mitglied
}
// Mitglieder vom Backend laden und ans Beispiel anhängen
const ladeMitglieder = async () => {
  try {
    const res = await fetch('/api/mitglied/all')
    if (res.ok) {
      const data = await res.json()
      const backendMitglieder = data.map((m) => ({
        id: m.id,
        mitgliedsnummer: String(m.mitgliedsnummer),
        hatErfahrung: m.erfahrung,
      }))
      if (backendMitglieder.length > 0) {
        mitgliederListe.value = backendMitglieder
      }
    }
  } catch (e) {
    console.error('Fehler beim Laden der Mitglieder (Beispiel-Mitglied bleibt erhalten):', e)
  }
}

onMounted(ladeMitglieder)

const kannWeiter = computed(() => !!ausgewaehltesMitglied.value && !!auswahl.value)

function weiterZuHundewahl() {
  if (!kannWeiter.value) return
  localStorage.setItem(
    'terminData',
    JSON.stringify({
      mitgliedId: ausgewaehltesMitglied.value.id,
      mitgliedsnummer: ausgewaehltesMitglied.value.mitgliedsnummer,
      ...auswahl.value,
    }),
  )
  router.push('/app/admin/hundewahl')
}
</script>
