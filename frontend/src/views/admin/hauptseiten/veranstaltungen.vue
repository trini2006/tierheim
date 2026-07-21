<template>
  <div>
    <h2 class="text-lg font-bold text-gray-800 mb-4">Veranstaltungen</h2>

    <!-- Hinzufügen-Zeile: Der Plus-Button leitet jetzt direkt weiter -->
    <div class="flex items-center justify-between mb-3">
      <h3 class="text-sm tracking-wide uppercase text-gray-600">Hinzufügen</h3>
      <button
        @click="geheZuHinzufuegen"
        class="text-2xl leading-none text-gray-700 hover:text-gray-900"
        aria-label="Neue Veranstaltung hinzufügen"
      >
        +
      </button>
    </div>

    <!-- Liste der Veranstaltungen -->
    <div class="space-y-3">
      <div
        v-for="veranstaltung in veranstaltungen"
        :key="veranstaltung.id"
        class="flex items-center justify-between p-2 pr-3 rounded-full bg-[#D3DDD1] border border-gray-200 shadow-sm"
      >
        <div class="flex items-center gap-3">
          <div
            class="w-10 h-10 rounded-full bg-red-700 text-white flex items-center justify-center text-[9px] font-bold text-center leading-tight shrink-0"
          >
            {{ veranstaltung.kuerzel }}
          </div>
          <div class="leading-tight">
            <span class="block text-sm font-semibold text-gray-800">{{ veranstaltung.titel }}</span>
            <span class="block text-xs text-gray-600">{{ veranstaltung.datum }}</span>
          </div>
        </div>
        <div class="flex items-center gap-3">
          <button @click="bearbeiten(veranstaltung)" aria-label="Veranstaltung bearbeiten">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4 text-gray-800">
              <path stroke-linecap="round" stroke-linejoin="round" d="m16.862 4.487 1.687-1.688a1.875 1.875 0 1 1 2.652 2.652L10.582 16.07a4.5 4.5 0 0 1-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 0 1 1.13-1.897l8.932-8.931Zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0 1 15.75 21H5.25A2.25 2.25 0 0 1 3 18.75V8.25A2.25 2.25 0 0 1 5.25 6H10" />
            </svg>
          </button>
          <button @click="loeschen(veranstaltung.id)" aria-label="Veranstaltung löschen">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4 text-gray-800">
              <path stroke-linecap="round" stroke-linejoin="round" d="m14.74 9-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 0 1-2.244 2.077H8.084a2.25 2.25 0 0 1-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 0 0-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 0 1 3.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 0 0-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916m7.5 0a48.667 48.667 0 0 0-7.5 0" />
            </svg>
          </button>
        </div>
      </div>

      <p v-if="veranstaltungen.length === 0" class="text-sm text-gray-500 italic">
        Aktuell keine geplanten Veranstaltungen...
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const veranstaltungen = ref([
  { id: 1, titel: 'Tag der offenen Tür', datum: '31.05.2026', kuerzel: 'Tag der offenen Tür' },
  { id: 2, titel: 'Arbeitseinsatz - Pool', datum: '30.07.2026', kuerzel: 'Arbeitseinsatz' },
])

// Funktion für den Plus-Button
const geheZuHinzufuegen = () => {
  router.push('/admin/veranstaltungen/hinzufuegen')
}

const bearbeiten = (veranstaltung) => {
  // Wenn du möchtest, kannst du hier beim Bearbeiten ebenfalls zur Hinzufügen-Seite weiterleiten
  router.push('/admin/veranstaltungen/hinzufuegen')
}

const loeschen = (id) => {
  veranstaltungen.value = veranstaltungen.value.filter((v) => v.id !== id)
}
</script>