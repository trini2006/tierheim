<template>
  <div class="w-full bg-white px-12 py-10">
    <div class="mx-auto flex max-w-5xl gap-16">
      
      <aside class="flex w-44 flex-col items-center">
        <div class="mb-5 h-36 w-36 rounded-full border border-gray-300 bg-gray-50">
          </div>
        <button class="rounded bg-gray-200 px-4 py-2 text-sm text-gray-800 transition-colors hover:bg-gray-300">
          Bild bearbeiten
        </button>
      </aside>

      <main class="flex-1">
        
        <section class="mb-8">
          <div class="flex items-center gap-3">
            <h2 v-if="!isEditing.title" class="text-2xl font-bold text-black">
              {{ profile.name }}
            </h2>
            <input 
              v-else 
              v-model="profile.name" 
              class="rounded border border-gray-300 px-2 py-1 text-2xl font-bold text-black focus:border-[#8B2626] focus:outline-none"
            />
            
            <button 
              class="text-gray-800 hover:text-black focus:outline-none" 
              @click="toggleEdit('title')"
              :aria-label="isEditing.title ? 'Speichern' : 'Bearbeiten'"
            >
              <i v-if="!isEditing.title" class="fas fa-pencil-alt text-lg"></i>
              <span v-else class="text-lg">💾</span>
            </button>
          </div>

          <div class="mt-1.5 flex items-center gap-2.5 text-sm text-gray-700">
            <span>Leitfarbe</span>
            <span class="h-4 w-4 rounded-full bg-[#8B2626] inline-block"></span>
          </div>
        </section>

        <div class="grid grid-cols-2 gap-x-16 gap-y-8">
          
          <div>
            <div class="flex items-center gap-3 mb-2">
              <h3 class="text-lg font-bold text-black">Kontaktdaten</h3>
              <button 
                class="text-gray-800 hover:text-black focus:outline-none" 
                @click="toggleEdit('contact')"
              >
                <i v-if="!isEditing.contact" class="fas fa-pencil-alt text-sm"></i>
                <span v-else class="text-sm">💾</span>
              </button>
            </div>

            <div v-if="!isEditing.contact" class="space-y-1 text-sm text-gray-900 leading-normal">
              <p>{{ profile.contact.street }}</p>
              <p>{{ profile.contact.zip }} {{ profile.contact.city }}</p>
              <p>{{ profile.contact.phone }}</p>
              <p>{{ profile.contact.emergency }} (Notfall)</p>
              <p class="underline cursor-pointer text-black">{{ profile.contact.website }}</p>
            </div>

            <div v-else class="space-y-2 text-sm">
              <input v-model="profile.contact.street" class="w-full rounded border px-2 py-1 focus:outline-none focus:border-[#8B2626]" />
              <div class="flex gap-2">
                <input v-model="profile.contact.zip" class="w-20 rounded border px-2 py-1 focus:outline-none focus:border-[#8B2626]" />
                <input v-model="profile.contact.city" class="flex-1 rounded border px-2 py-1 focus:outline-none focus:border-[#8B2626]" />
              </div>
              <input v-model="profile.contact.phone" class="w-full rounded border px-2 py-1 focus:outline-none focus:border-[#8B2626]" />
              <input v-model="profile.contact.emergency" class="w-full rounded border px-2 py-1 focus:outline-none focus:border-[#8B2626]" />
              <input v-model="profile.contact.website" class="w-full rounded border px-2 py-1 focus:outline-none focus:border-[#8B2626]" />
            </div>
          </div>

          <div>
            <div class="flex items-center gap-3 mb-2">
              <h3 class="text-lg font-bold text-black">Kalenderdaten</h3>
              <button 
                class="text-gray-800 hover:text-black focus:outline-none" 
                @click="toggleEdit('calendar')"
              >
                <i v-if="!isEditing.calendar" class="fas fa-pencil-alt text-sm"></i>
                <span v-else class="text-sm">💾</span>
              </button>
            </div>

            <div v-if="!isEditing.calendar" class="space-y-1.5 text-sm text-gray-900">
              <div class="flex items-center gap-3 mb-3">
                <span>Zeitblock:</span>
                <div class="flex gap-1.5">
                  <span 
                    v-for="block in [30, 45, 60]" 
                    :key="block"
                    class="rounded px-2.5 py-1 text-xs"
                    :class="profile.calendar.timeBlock === block ? 'bg-[#798b7a] text-white' : 'bg-gray-200 text-gray-800'"
                  >
                    {{ block }}
                  </span>
                </div>
              </div>
              <p>Standard Ruhezeiten (Hund): {{ profile.calendar.restTime }}</p>
              <p>max. Gassi: {{ profile.calendar.maxGassi }}</p>
              <p>min. Gassi: {{ profile.calendar.minGassi }}</p>
            </div>

            <div v-else class="space-y-2 text-sm">
              <div class="flex items-center gap-3 mb-2">
                <span>Zeitblock:</span>
                <div class="flex gap-1.5">
                  <button 
                    v-for="block in [30, 45, 60]" 
                    :key="block"
                    type="button"
                    @click="profile.calendar.timeBlock = block"
                    class="rounded px-2.5 py-1 text-xs transition-colors"
                    :class="profile.calendar.timeBlock === block ? 'bg-[#798b7a] text-white' : 'bg-gray-200 text-gray-800'"
                  >
                    {{ block }}
                  </button>
                </div>
              </div>
              <div class="flex items-center gap-2">
                <label class="whitespace-nowrap">Ruhezeiten:</label>
                <input v-model.number="profile.calendar.restTime" type="number" class="w-20 rounded border px-2 py-1" />
              </div>
              <div class="flex items-center gap-2">
                <label class="whitespace-nowrap">max. Gassi:</label>
                <input v-model.number="profile.calendar.maxGassi" type="number" class="w-20 rounded border px-2 py-1" />
              </div>
              <div class="flex items-center gap-2">
                <label class="whitespace-nowrap">min. Gassi:</label>
                <input v-model.number="profile.calendar.minGassi" type="number" class="w-20 rounded border px-2 py-1" />
              </div>
            </div>
          </div>

          <div>
            <div class="flex items-center gap-3 mb-2">
              <h3 class="text-lg font-bold text-black">Gassi Zeiten</h3>
              <button 
                class="text-gray-800 hover:text-black focus:outline-none" 
                @click="toggleEdit('gassi')"
              >
                <i v-if="!isEditing.gassi" class="fas fa-pencil-alt text-sm"></i>
                <span v-else class="text-sm">💾</span>
              </button>
            </div>

            <div v-if="!isEditing.gassi" class="space-y-1.5 text-sm text-gray-900">
              <div v-for="(slot, idx) in profile.gassiTimes" :key="idx" class="flex gap-6">
                <span class="w-6 font-bold">{{ slot.day }}</span>
                <span>{{ slot.from }} - {{ slot.to }} Uhr</span>
              </div>
            </div>

            <div v-else class="space-y-2 text-sm">
              <div v-for="(slot, idx) in profile.gassiTimes" :key="idx" class="flex items-center gap-2">
                <input v-model="slot.day" class="w-10 font-bold rounded border px-1 py-0.5 text-center" />
                <input v-model="slot.from" class="w-16 rounded border px-1 py-0.5" />
                <span>-</span>
                <input v-model="slot.to" class="w-16 rounded border px-1 py-0.5" />
              </div>
            </div>
          </div>

        </div>
      </main>

    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue'

const profile = reactive({
  name: 'Tierheim Weiden',
  contact: {
    street: 'Schustermooslohe 96',
    zip: '92637',
    city: 'Weiden i. d. Oberpfalz',
    phone: '0961 25780',
    emergency: '0176 47159671',
    website: 'tierheim-weiden.de'
  },
  calendar: {
    timeBlock: 30,
    restTime: 60,
    maxGassi: 120,
    minGassi: 30
  },
  gassiTimes: [
    { day: 'Mo', from: '10:00', to: '14:00' },
    { day: 'Mi', from: '10:00', to: '14:00' },
    { day: 'Fr', from: '10:00', to: '14:00' },
    { day: 'Sa', from: '10:00', to: '11:30' }
  ]
})

const isEditing = reactive({
  title: false,
  contact: false,
  calendar: false,
  gassi: false
})

const toggleEdit = (section) => {
  isEditing[section] = !isEditing[section]
}
</script>