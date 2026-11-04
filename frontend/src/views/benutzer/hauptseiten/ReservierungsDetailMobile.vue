<template>
  <div class="p-4 max-w-md mx-auto">
    <button @click="router.back()" class="text-sm text-gray-700 mb-4">
      ← Zurück zur Übersicht
    </button>

    <p v-if="store.fehler" class="text-sm text-red-700">{{ store.fehler }}</p>
    <p v-else-if="!store.geladen" class="text-sm text-gray-500">Lade...</p>
    <ReservierungsDetail v-else :reservierung="reservierung" />
  </div>
</template>

<script setup>
import { computed, watch, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useReservierungenStore } from '@/stores/reservierungen.js'
import { useIstMobil } from '@/composables/useIstMobil.js'
import ReservierungsDetail from './ReservierungsDetail.vue'

const route = useRoute()
const router = useRouter()
const store = useReservierungenStore()
const istMobil = useIstMobil()

onMounted(store.ladeFallsNoetig)

const reservierung = computed(
  () => store.alle.find((r) => String(r.id) === route.params.id) ?? null,
)

// Fenster wird auf Desktop-Breite aufgezogen -> zurück zur Startseite
watch(istMobil, (mobil) => {
  if (!mobil) router.replace('/app')
})
</script>
