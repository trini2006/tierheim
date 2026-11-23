<template>
  <div class="max-w-md md:max-w-2xl mx-auto p-4 md:p-6 space-y-4">
    <div class="flex items-center justify-between">
      <button @click="router.back()" class="text-sm text-gray-700">← Zurück</button>
      <button
        v-if="store.ungelesen > 0"
        @click="store.markiereAlleGelesen()"
        class="text-sm text-emerald-800 hover:underline"
      >
        Alle als gelesen markieren
      </button>
    </div>

    <h1 class="text-lg font-bold text-gray-800">Nachrichten</h1>

    <p v-if="store.fehler" class="text-sm text-red-700">{{ store.fehler }}</p>
    <p v-else-if="!store.geladen" class="text-sm text-gray-500">Lade...</p>
    <p v-else-if="store.alle.length === 0" class="text-sm text-gray-500 italic">
      Keine Nachrichten vorhanden.
    </p>

    <ul v-else class="space-y-3">
      <li v-for="n in store.alle" :key="n.id">
        <button
          @click="oeffne(n)"
          :class="[
            'w-full text-left p-4 rounded-2xl border shadow-sm transition-colors',
            n.gelesen
              ? 'bg-gray-50 border-gray-200'
              : 'bg-[#D3DDD1] border-emerald-700 hover:bg-[#c2cebf]',
          ]"
        >
          <div class="flex items-start gap-3">
            <span
              v-if="!n.gelesen"
              class="mt-1.5 w-2.5 h-2.5 rounded-full bg-red-500 flex-shrink-0"
            ></span>
            <div class="flex-grow">
              <p :class="['text-sm text-gray-900', n.gelesen ? '' : 'font-semibold']">
                {{ n.text }}
              </p>
              <p class="text-xs text-gray-500 mt-1">{{ formatiere(n.erstelltAm) }}</p>
            </div>
          </div>
        </button>
      </li>
    </ul>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useBenachrichtigungenStore } from '@/stores/benachrichtigungen.js'

const router = useRouter()
const store = useBenachrichtigungenStore()

onMounted(store.lade)

const oeffne = (n) => {
  if (!n.gelesen) store.markiereGelesen(n.id)
}

const formatiere = (iso) =>
  new Date(iso).toLocaleString('de-DE', { dateStyle: 'short', timeStyle: 'short' }) + ' Uhr'
</script>
