<!--muss noch für admin gemacht werden, aber die vorherige
      seiten sind noch nicht fertig-->

<template>
  <div class="flex w-full h-full p-4 md:p-8">
    <!-- LINKS: Die Liste (auf Desktop sichtbar) -->
    <div class="hidden md:block w-1/3 pr-8 border-r">
      <h3 class="text-xl font-bold text-gray-800 mb-6 font-sans">Unsere Hunde</h3>
      <div class="space-y-3">
        <div
          v-for="h in alleHunde"
          :key="h.id"
          @click="waehle(h.id)"
          :class="[
            'p-3 rounded-full cursor-pointer transition-all flex items-center justify-between border-2',
            istAusgewaehlt(h)
              ? 'bg-[#C5D0C5] border-emerald-700'
              : 'bg-gray-100 border-transparent hover:bg-gray-200',
          ]"
        >
          <span class="font-bold text-sm">{{ h.name }}</span>
          <span v-if="istAusgewaehlt(h)">›</span>
        </div>
      </div>
    </div>

    <!-- RECHTS: Die Details (dein Design) -->
    <div class="w-full md:w-2/3 md:pl-8">
      <div v-if="angezeigterHund" class="max-w-md mx-auto">
        <!-- Bildbereich -->
        <div class="flex flex-col items-center">
          <img
            v-if="angezeigterHund.bildURL"
            :src="angezeigterHund.bildURL"
            :alt="angezeigterHund.name"
            class="w-full h-64 object-cover rounded-2xl shadow-md"
          />
          <div
            v-else
            class="w-full h-64 rounded-2xl bg-gray-200 flex items-center justify-center text-5xl"
          >
            🐾
          </div>
        </div>

        <!-- Beschreibung Box -->
        <div class="bg-[#C5D0C5] p-6 rounded-3xl mt-8">
          <div class="flex justify-between items-start">
            <h2 class="text-xl font-bold mb-4">Beschreibung</h2>
            <div
              :class="[
                'w-4 h-4 rounded-full mt-1 flex-shrink-0',
                angezeigterHund.erfahrung ? 'bg-orange-500' : 'bg-green-500',
              ]"
            />
          </div>
          <div class="grid grid-cols-2 gap-y-2 text-sm">
            <p class="font-bold">Alter:</p>
            <p>{{ angezeigterHund.jahre }}</p>
            <p class="font-bold">Rasse:</p>
            <p>{{ angezeigterHund.rasse }}</p>
          </div>
          <div class="flex gap-2 mt-4">
            <span
              v-for="label in angezeigterHund?.labels"
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
  </div>
</template>

<script setup>
import { onMounted, ref, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
const route = useRoute()
const router = useRouter()

const tierheimId = 1
const alleHunde = ref([])
const geladen = ref(false)

const ladeHunde = async () => {
  try {
    const response = await fetch(`/api/hund/${tierheimId}/all`)
    if (response.ok) alleHunde.value = await response.json()
    else console.error('Fehler beim Laden der Hunde:', response.status)
  } catch (error) {
    console.error('Verbindungsfehler zum Backend:', error)
  } finally {
    geladen.value = true
  }
}
onMounted(ladeHunde)

// gewählter Hund laut URL, sonst der erste der Liste
const angezeigterHund = computed(
  () => alleHunde.value.find((h) => String(h.id) === route.params.id) ?? alleHunde.value[0] ?? null,
)

const istAusgewaehlt = (h) => h.id === angezeigterHund.value?.id
const waehle = (id) => router.push(`/app/hund/${id}`)

// URL mit Anzeige synchron halten (auch bei unbekannter ID)
watch([() => route.params.id, angezeigterHund], ([id, h]) => {
  if (h && String(h.id) !== id) router.replace(`/app/hund/${h.id}`)
})
</script>
