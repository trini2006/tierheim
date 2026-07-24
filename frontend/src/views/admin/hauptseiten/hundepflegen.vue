<template>
    <div class="max-w-4xl mx-auto p-6 space-y-8 bg-white min-h-screen relative">
      
      <!-- OBERE ZEILE: Name bearbeiten -->
      <div class="flex items-center gap-3">
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-6 h-6 text-gray-800">
          <path stroke-linecap="round" stroke-linejoin="round" d="m16.862 4.487 1.687-1.688a1.875 1.875 0 1 1 2.652 2.652L10.582 16.07a4.5 4.5 0 0 1-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 0 1 1.13-1.897l8.932-8.931Zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0 1 15.75 21H5.25A2.25 2.25 0 0 1 3 18.75V8.25A2.25 2.25 0 0 1 5.25 6H10" />
        </svg>
        <input 
          v-model="hund.name" 
          type="text" 
          placeholder="Name des Hundes"
          class="text-2xl font-bold text-gray-800 border-b border-gray-300 focus:outline-none focus:border-emerald-700 bg-transparent px-1 py-0.5 w-full max-w-xs"
        />
      </div>
  
      <div class="grid grid-cols-1 md:grid-cols-2 gap-12 items-start">
        
        <!-- LINKE SEITE: Geschlecht, Erfahrung & Bilder -->
        <div class="space-y-6">
          
          <!-- Geschlecht Auswahl -->
          <div class="flex items-center gap-6">
            <span class="font-semibold text-gray-700 w-24">Geschlecht:</span>
            <div class="flex items-center gap-4">
              <button 
                type="button" 
                @click="hund.gender = 'Rüde'"
                :class="['text-2xl p-2 rounded-full border transition-colors', hund.gender === 'Rüde' ? 'border-blue-600 bg-blue-50 text-blue-600' : 'border-gray-300 text-gray-400']"
                title="Rüde"
              >
                ♂
              </button>
              <button 
                type="button" 
                @click="hund.gender = 'Hündin'"
                :class="['text-2xl p-2 rounded-full border transition-colors', hund.gender === 'Hündin' ? 'border-pink-600 bg-pink-50 text-pink-600' : 'border-gray-300 text-gray-400']"
                title="Hündin"
              >
                ♀
              </button>
            </div>
          </div>
  
          <!-- Erfahrung / Statuspunkt Auswahl -->
          <div class="flex items-center gap-6">
            <span class="font-semibold text-gray-700 w-24">Erfahrung:</span>
            <div class="flex items-center gap-4">
              <button 
                type="button" 
                @click="hund.statusPunkt = 'bg-green-500'"
                :class="['w-6 h-6 rounded-full bg-green-500 transition-transform', hund.statusPunkt === 'bg-green-500' ? 'scale-125 ring-2 ring-offset-2 ring-green-600' : 'opacity-60']"
              ></button>
              <button 
                type="button" 
                @click="hund.statusPunkt = 'bg-orange-400'"
                :class="['w-6 h-6 rounded-full bg-orange-400 transition-transform', hund.statusPunkt === 'bg-orange-400' ? 'scale-125 ring-2 ring-offset-2 ring-orange-500' : 'opacity-60']"
              ></button>
            </div>
          </div>
  
          <!-- Bilder Vorschau -->
          <div class="space-y-3">
            <div class="flex gap-4 items-center">
              <img :src="hund.bild" class="w-36 h-36 rounded-2xl object-cover shadow-sm border" />
              <div class="grid grid-cols-2 gap-2">
                <div class="w-16 h-16 bg-gray-200 rounded-lg overflow-hidden border">
                  <img :src="hund.bild" class="w-full h-full object-cover" />
                </div>
                <div class="w-16 h-16 bg-gray-200 rounded-lg border"></div>
              </div>
            </div>
            <button type="button" class="px-4 py-2 bg-gray-200 text-gray-800 text-sm font-semibold rounded-lg hover:bg-gray-300 transition-colors">
              Bilder bearbeiten
            </button>
          </div>
  
        </div>
  
        <!-- RECHTE SEITE: Beschreibung (Alter, Rasse, Gewicht, Strecke) -->
        <div class="space-y-6">
          
          <div class="bg-[#BFCABF] p-6 rounded-3xl shadow-sm space-y-4">
            <h3 class="text-xl font-bold text-gray-800 mb-2">Beschreibung</h3>
            
            <div class="flex items-center justify-between border-b border-gray-300/60 pb-3">
              <span class="font-medium text-gray-700">Alter:</span>
              <div class="flex items-center gap-2">
                <input v-model="hund.age" type="text" placeholder="z.B. 2 Jahre" class="bg-white/60 px-2 py-1 rounded text-right text-sm focus:outline-none w-28" />
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4 text-gray-600"><path stroke-linecap="round" stroke-linejoin="round" d="m16.862 4.487 1.687-1.688a1.875 1.875 0 1 1 2.652 2.652L10.582 16.07a4.5 4.5 0 0 1-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 0 1 1.13-1.897l8.932-8.931Zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0 1 15.75 21H5.25A2.25 2.25 0 0 1 3 18.75V8.25A2.25 2.25 0 0 1 5.25 6H10" /></svg>
              </div>
            </div>
  
            <div class="flex items-center justify-between border-b border-gray-300/60 pb-3">
              <span class="font-medium text-gray-700">Rasse:</span>
              <div class="flex items-center gap-2">
                <input v-model="hund.breed" type="text" placeholder="z.B. Mischling" class="bg-white/60 px-2 py-1 rounded text-right text-sm focus:outline-none w-36" />
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4 text-gray-600"><path stroke-linecap="round" stroke-linejoin="round" d="m16.862 4.487 1.687-1.688a1.875 1.875 0 1 1 2.652 2.652L10.582 16.07a4.5 4.5 0 0 1-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 0 1 1.13-1.897l8.932-8.931Zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0 1 15.75 21H5.25A2.25 2.25 0 0 1 3 18.75V8.25A2.25 2.25 0 0 1 5.25 6H10" /></svg>
              </div>
            </div>
  
            <div class="flex items-center justify-between border-b border-gray-300/60 pb-3">
              <span class="font-medium text-gray-700">Gewicht:</span>
              <div class="flex items-center gap-2">
                <input v-model="hund.weight" type="text" placeholder="z.B. 25kg" class="bg-white/60 px-2 py-1 rounded text-right text-sm focus:outline-none w-28" />
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4 text-gray-600"><path stroke-linecap="round" stroke-linejoin="round" d="m16.862 4.487 1.687-1.688a1.875 1.875 0 1 1 2.652 2.652L10.582 16.07a4.5 4.5 0 0 1-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 0 1 1.13-1.897l8.932-8.931Zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0 1 15.75 21H5.25A2.25 2.25 0 0 1 3 18.75V8.25A2.25 2.25 0 0 1 5.25 6H10" /></svg>
              </div>
            </div>
  
            <div class="flex items-center justify-between">
              <span class="font-medium text-gray-700">Strecke:</span>
              <div class="flex items-center gap-2">
                <input v-model="hund.strecke" type="text" placeholder="z.B. lang" class="bg-white/60 px-2 py-1 rounded text-right text-sm focus:outline-none w-28" />
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4 text-gray-600"><path stroke-linecap="round" stroke-linejoin="round" d="m16.862 4.487 1.687-1.688a1.875 1.875 0 1 1 2.652 2.652L10.582 16.07a4.5 4.5 0 0 1-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 0 1 1.13-1.897l8.932-8.931Zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0 1 15.75 21H5.25A2.25 2.25 0 0 1 3 18.75V8.25A2.25 2.25 0 0 1 5.25 6H10" /></svg>
              </div>
            </div>
          </div>
  
          <!-- Übernehmen Button -->
          <div class="flex justify-end">
            <button 
              type="button" 
              @click="speichern"
              class="px-8 py-3 bg-[#8FA18C] hover:bg-[#7e917b] text-white font-bold rounded-xl shadow transition-colors"
            >
              Übernehmen
            </button>
          </div>
  
        </div>
      </div>
  
      <!-- UNTEN: Tags / Eigenschaften -->
      <div class="pt-6">
        <div class="flex flex-wrap items-center gap-3">
          <div v-for="(tag, index) in hund.tags" :key="index" class="flex items-center bg-gray-200 px-3 py-1.5 rounded-full text-sm font-medium text-gray-700 shadow-sm">
            <span>{{ tag }}</span>
            <button @click="entferneTag(index)" class="ml-2 w-4 h-4 bg-red-600 text-white rounded-full text-xs flex items-center justify-center hover:bg-red-700">✕</button>
          </div>
          <button @click="neuenTagHinzufuegen" class="w-8 h-8 rounded-full bg-gray-200 hover:bg-gray-300 flex items-center justify-center text-gray-700 font-bold text-lg shadow-sm">+</button>
        </div>
      </div>
  
      <!-- POP-UP MODAL (Erfolgsmeldung) -->
      <div v-if="popUfZaehler" class="fixed inset-0 bg-black/40 flex items-center justify-center z-50">
        <div class="bg-white p-6 rounded-2xl shadow-2xl text-center space-y-4 max-w-sm w-full mx-4 animate-bounce">
          <div class="w-12 h-12 bg-emerald-100 text-emerald-600 rounded-full flex items-center justify-center mx-auto text-2xl font-bold">✓</div>
          <h4 class="text-lg font-bold text-gray-800">{{ popupMeldung }}</h4>
        </div>
      </div>
  
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue'
  import { useRoute, useRouter } from 'vue-router'
  
  const route = useRoute()
  const router = useRouter()
  
  const isEditMode = ref(false)
  const popupMeldung = ref('')
  const popUfZaehler = ref(false)
  
  // Formular Datenstruktur
  const hund = ref({
    id: null,
    name: '',
    gender: 'Rüde',
    statusPunkt: 'bg-green-500',
    age: '',
    breed: '',
    weight: '',
    strecke: '',
    bild: 'https://images.unsplash.com/photo-1543466835-00a7907e9de1',
    tags: ['ruhig', 'zurückhaltend', 'freundlich']
  })
  
  onMounted(() => {
    const editData = localStorage.getItem('editHund')
    if (editData) {
      // Bearbeiten-Modus: Daten laden
      isEditMode.value = true
      hund.value = JSON.parse(editData)
    } else {
      // Neuer Hund Modus: Felder sind leer
      isEditMode.value = false
      hund.value = {
        id: Date.now(),
        name: '',
        gender: 'Rüde',
        statusPunkt: 'bg-green-500',
        age: '',
        breed: '',
        weight: '',
        strecke: '',
        bild: 'https://images.unsplash.com/photo-1543466835-00a7907e9de1',
        tags: []
      }
    }
  })
  
  const enferneTag = (index) => {
    hund.value.tags.splice(index, 1)
  }
  
  const neuenTagHinzufuegen = () => {
    const neuerTag = prompt('Neues Tag eingeben:')
    if (neuerTag) {
      hund.value.tags.push(neuerTag)
    }
  }
  
  const speichern = () => {
    if (!hund.value.name.trim()) {
      alert('Bitte geben Sie einen Namen ein.')
      return
    }
  
    // Bestehende Liste aus localStorage holen oder initialisieren
    let gespeicherteHunde = JSON.parse(localStorage.getItem('alleHundeListe') || '[]')
  
    if (isEditMode.value) {
      // Hund in der Liste aktualisieren
      const index = gespeicherteHunde.findIndex(h => h.id === hund.value.id)
      if (index !== -1) {
        gespeicherteHunde[index] = hund.value
      } else {
        gespeicherteHunde.push(hund.value)
      }
      popupMeldung.value = 'Hund wurde geändert'
    } else {
      // Neuen Hund hinzufügen
      gespeicherteHunde.push(hund.value)
      popupMeldung.value = 'Hund wurde angelegt'
    }
  
    localStorage.setItem('alleHundeListe', JSON.stringify(gespeicherteHunde))
    localStorage.removeItem('editHund') // Aufräumen
  
    // Pop-up anzeigen und nach 1.5 Sekunden zur Übersicht (hunde.vue) wechseln
    popUfZaehler.value = true
    setTimeout(() => {
      popUfZaehler.value = false
      router.push('app//admin/hunde') // Passe den Pfad zu deiner Hunde-Übersicht an falls nötig
    }, 1500)
  }
  </script>