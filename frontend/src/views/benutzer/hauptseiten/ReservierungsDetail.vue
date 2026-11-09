<template>
  <div v-if="reservierung">
    <div class="bg-[#D3DDD1] p-5 rounded-2xl shadow-sm border border-gray-200">
      <div class="flex gap-4">
        <div
          class="w-28 h-28 rounded-xl overflow-hidden bg-[#94A684]/40 flex-shrink-0 flex items-center justify-center"
        >
          <img
            v-if="hund?.bild"
            :src="hund.bild"
            :alt="hund.name"
            class="w-full h-full object-cover"
          />
          <span v-else class="text-3xl">🐾</span>
        </div>

        <div class="flex-grow space-y-2 text-left relative">
          <span
            :class="[
              'absolute top-0 right-0 w-3 h-3 rounded-full',
              hund?.erfahrung ? 'bg-orange-500' : 'bg-green-500',
            ]"
          ></span>
          <h3 class="text-xl font-bold text-gray-900 leading-tight">{{ hund?.name || 'Hund' }}</h3>
          <p class="text-xs text-gray-600">
            {{ hund?.rasse }}<br />
            {{ hund?.jahre }} Jahre, {{ hund?.geschlecht ? 'Hündin' : 'Rüde' }}
          </p>
          <div class="flex flex-wrap gap-1.5 pt-1">
            <span
              v-for="label in hund?.labels"
              :key="label.id"
              :class="label.hinweis ? 'bg-orange-500/50' : 'bg-white/50'"
              class="px-2 py-0.5 rounded-full text-[11px] text-gray-700 font-medium"
            >
              {{ label.bezeichnung }}
            </span>
          </div>
        </div>
      </div>
    </div>

    <div class="mt-6 space-y-2 text-left px-2">
      <div class="flex text-sm">
        <span class="font-bold w-20 text-gray-700">Datum:</span>
        <span>{{ formatiereDatumLang(reservierung.datum) }}</span>
      </div>
      <div class="flex text-sm">
        <span class="font-bold w-20 text-gray-700">Uhrzeit:</span>
        <span
          >{{ formatiereUhrzeit(reservierung.zeitAb) }} -
          {{ formatiereUhrzeit(reservierung.zeitBis) }} Uhr</span
        >
      </div>
      <div class="flex text-sm">
        <span class="font-bold w-20 text-gray-700">Status:</span>
        <span :class="{ 'text-red-700 font-semibold': istStorniert }">{{
          reservierung.status
        }}</span>
      </div>
      <div v-if="istStorniert" class="flex text-sm">
        <span class="font-bold w-20 text-gray-700">Grund:</span>
        {{ reservierung.stornierungsgrund }}
      </div>
    </div>

    <div v-if="!istStorniert" class="mt-6 text-left px-2">
      <button
        @click="stornieren"
        :disabled="laeuft"
        class="bg-red-400 hover:bg-red-500 disabled:opacity-50 text-gray-900 font-bold px-6 py-2.5 rounded-full shadow-sm transition-colors text-sm"
      >
        Termin stornieren
      </button>
      <p v-if="fehlertext" class="text-sm text-red-700 mt-2">{{ fehlertext }}</p>
    </div>
  </div>

  <div v-else class="text-sm text-gray-500 italic p-4 text-left">Aktuell kein Termin geplant.</div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useReservierungenStore } from '@/stores/reservierungen.js'
import { formatiereDatumLang, formatiereUhrzeit } from '@/components/utils/datum.js'

const props = defineProps({ reservierung: { type: Object, default: null } })
const store = useReservierungenStore()

const hund = computed(() => props.reservierung?.hund)
const istStorniert = computed(() => props.reservierung?.status === 'STORNIERT')
const laeuft = ref(false)
const fehlertext = ref('')

const stornieren = async () => {
  if (!window.confirm('Diesen Termin wirklich stornieren?')) return
  laeuft.value = true
  fehlertext.value = ''
  try {
    await store.stornieren(props.reservierung.idReservierung)
  } catch (e) {
    fehlertext.value = e.message || 'Stornieren fehlgeschlagen.'
  } finally {
    laeuft.value = false
  }
}
</script>
