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
              :class="ausgewaehltesMitglied?.id === mitglied.id ? 'bg-[#b8c9b4] border-emerald-700 ring-2 ring-emerald-600' : 'bg-[#D3DDD1] border-gray-200 hover:bg-[#c2cebf]'"
            >
              <div class="flex items-center gap-3">
                <div class="w-10 h-10 rounded-full bg-emerald-900 flex items-center justify-center text-white font-bold">
                  👤
                </div>
                <span class="font-bold text-gray-800 text-sm md:text-base">{{ mitglied.telefon }}</span>
              </div>
              <span class="w-3 h-3 rounded-full" :class="mitglied.aktiv ? 'bg-green-500' : 'bg-orange-400'"></span>
            </div>
          </div>
        </div>
  
        <!-- RECHTE SEITE: Zeitwahl (Mehrfachauswahl, gesperrt solange kein Mitglied gewählt) -->
        <div :class="{'opacity-50 pointer-events-none': !ausgewaehltesMitglied}" class="transition-opacity">
          <div class="bg-white p-6 rounded-3xl border border-gray-100 shadow-sm">
            
            <div class="flex justify-end gap-2 mb-6">
              <button v-for="kw in ['KW 26', 'KW 27', 'KW 28']" :key="kw" class="px-3 py-1 bg-[#D3DDD1] rounded-lg text-xs font-bold text-gray-800">
                {{ kw }}
              </button>
            </div>
  
            <div class="flex items-center justify-between mb-6">
              <button class="font-bold text-lg">&lt;</button>
              <span class="px-4 py-1.5 bg-[#D3DDD1] rounded-xl text-sm font-bold text-gray-800">Mi 01.07.</span>
              <button class="font-bold text-lg">&gt;</button>
            </div>
  
            <!-- Zeitblöcke (Mehrfachauswahl per Array) -->
            <div class="space-y-3">
              <div v-for="zeit in zeitBloecke" :key="zeit.id" class="flex items-center justify-between">
                <span class="text-xs text-gray-600 w-24">{{ zeit.label }}</span>
                <div 
                  @click="toggleZeit(zeit.id)"
                  class="flex-1 ml-4 h-10 rounded-xl cursor-pointer transition-colors flex items-center px-4 text-xs font-bold"
                  :class="ausgewaehlteZeiten.includes(zeit.id) ? 'bg-emerald-700 text-white' : 'bg-gray-200 hover:bg-gray-300 text-gray-700'"
                >
                  {{ ausgewaehlteZeiten.includes(zeit.id) ? 'Ausgewählt' : '' }}
                </div>
              </div>
            </div>
  
          </div>
        </div>
  
      </div>
  
      <!-- UNTERER BEREICH: Weiter-Button -->
      <div class="flex justify-end mt-8">
        <button 
          @click="weiterZuHundewahl"
          :disabled="!ausgewaehltesMitglied || ausgewaehlteZeiten.length === 0"
          class="flex items-center gap-3 px-8 py-3 rounded-full shadow-md font-bold transition-all"
          :class="(!ausgewaehltesMitglied || ausgewaehlteZeiten.length === 0) ? 'bg-gray-300 text-gray-500 cursor-not-allowed' : 'bg-[#D3DDD1] hover:bg-[#c2cebf] text-gray-800 border border-gray-300'"
        >
          <span>Weiter</span>
          <div class="w-8 h-8 rounded-full bg-emerald-600 flex items-center justify-center text-white">
            ✓
          </div>
        </button>
      </div>
  
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue'
  import { useRouter } from 'vue-router'
  
  const router = useRouter()
  
  const mitgliederListe = ref([
    { id: 1, telefon: '1234556789', aktiv: false },
    { id: 2, telefon: '1234567890', aktiv: true },
    { id: 3, telefon: '6371947129', aktiv: false },
    { id: 4, telefon: '3478123674', aktiv: true },
    { id: 5, telefon: '34712375123', aktiv: true },
  ])
  
  const ausgewaehltesMitglied = ref(null)
  // Geändert zu einem Array für mehrere Zeitblöcke
  const ausgewaehlteZeiten = ref([])
  
  const zeitBloecke = ref([
    { id: 1, label: '10:00 - 10:30 Uhr' },
    { id: 2, label: '10:30 - 11:00 Uhr' },
    { id: 3, label: '11:00 - 11:30 Uhr' },
    { id: 4, label: '11:30 - 12:00 Uhr' },
    { id: 5, label: '12:00 - 12:30 Uhr' },
    { id: 6, label: '12:30 - 13:00 Uhr' },
    { id: 7, label: '13:00 - 13:30 Uhr' },
    { id: 8, label: '13:30 - 14:00 Uhr' },
  ])
  
  function waehleMitglied(mitglied) {
    ausgewaehltesMitglied.value = mitglied
  }
  
  // Logik zum Hinzufügen oder Entfernen von Zeitblöcken (Mehrfachauswahl)
  function toggleZeit(zeitId) {
    if (!ausgewaehltesMitglied.value) return
    
    const index = ausgewaehlteZeiten.value.indexOf(zeitId)
    if (index > -1) {
      ausgewaehlteZeiten.value.splice(index, 1) // Entfernen, falls schon ausgewählt
    } else {
      ausgewaehlteZeiten.value.push(zeitId) // Hinzufügen
    }
  }
  
  function weiterZuHundewahl() {
  if (ausgewaehltesMitglied.value && ausgewaehlteZeiten.value.length > 0) {
    // Speichere die Daten zwischen (oder nutze Query-Parameter)
    const daten = {
      mitgliedId: ausgewaehltesMitglied.value.id,
      telefon: ausgewaehltesMitglied.value.telefon,
      zeiten: ausgewaehlteZeiten.value
    }
    localStorage.setItem('terminData', JSON.stringify(daten))

    // Führe den Admin zur Admin-Hundewahl statt zur Benutzer-Hundewahl
    router.push('/admin/hundewahl')
  }
}
  </script>