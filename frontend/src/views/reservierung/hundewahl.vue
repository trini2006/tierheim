<template>
  <div class="max-w-md mx-auto p-4 space-y-6 pb-24">
    <h2 class="text-xl font-bold">Unser Vorschlag</h2>
    <div
      v-if="vorschlagHund"
      @click="selectDog(vorschlagHund)"
      class="bg-[#BFCABF] p-4 rounded-xl cursor-pointer hover:bg-[#b0bcaf] transition-colors relative"
    >
      <div class="flex gap-4">
        <img
          v-if="vorschlagHund.image"
          :src="vorschlagHund.image"
          :alt="vorschlagHund.name"
          class="w-24 h-24 rounded-lg object-cover pointer-events-none flex-shrink-0"
        />
        <div
          v-else
          class="w-24 h-24 rounded-lg bg-[#94A684]/40 flex items-center justify-center text-3xl flex-shrink-0"
        >
          🐾
        </div>
        <div class="flex-grow">
          <div class="flex justify-between items-start">
            <h3 class="text-lg font-bold">{{ vorschlagHund.name }}</h3>
            <div :class="['w-4 h-4 rounded-full', vorschlagHund.erfahrung]" />
          </div>
          <p class="text-sm">
            {{ vorschlagHund.breed }} • {{ vorschlagHund.age }} Jahre, {{ vorschlagHund.gender }}
          </p>
          <div class="flex gap-2 mt-2 flex-wrap">
            <span
              v-for="tag in vorschlagHund.tags"
              :key="tag.id"
              :class="tag.hinweis ? 'bg-orange-500/50' : 'bg-white/50'"
              class="px-2 py-0.5 rounded-full text-xs"
              >{{ tag.name }}</span
            >
          </div>
        </div>
      </div>
    </div>

    <!-- SUCHE -->
    <div class="relative">
      <input
        v-model="searchQuery"
        type="text"
        placeholder="Suche nach Name..."
        class="w-full p-3 rounded-full bg-gray-200 pl-10"
      />
    </div>

    <!-- LISTE FREIER HUNDE -->
    <h2 class="text-xl font-bold">Freie Hunde</h2>
    <h3 v-if="terminData"  class="text-sm font-bold text-gray-700 uppercase tracking-wide">
      {{ formatiereUhrzeit(terminData.von) }} – {{ formatiereUhrzeit(terminData.bis) }} Uhr
    </h3>

    <div
      v-for="dog in filteredDogs"
      :key="dog.id"
      @click="selectDog(dog)"
      class="bg-[#BFCABF] p-4 rounded-xl cursor-pointer flex gap-4 hover:bg-[#b0bcaf] transition-colors relative"
    >
      <img
        v-if="dog.image"
        :src="dog.image"
        :alt="dog.name"
        class="w-24 h-24 rounded-lg object-cover pointer-events-none flex-shrink-0"
      />
      <div
        v-else
        class="w-24 h-24 rounded-lg bg-[#94A684]/40 flex items-center justify-center text-3xl flex-shrink-0"
      >
        🐾
      </div>

      <div class="flex-grow">
        <h3 class="text-lg font-bold">{{ dog.name }}</h3>
        <p class="text-sm">{{ dog.breed }} • {{ dog.age }} Jahre, {{ dog.gender }}</p>
        <div class="flex gap-2 mt-2 flex-wrap">
          <span
            v-for="tag in dog.tags"
            :key="tag.id"
            :class="tag.hinweis ? 'bg-orange-500/50' : 'bg-white/50'"
            class="px-2 py-0.5 rounded-full text-xs"
            >{{ tag.name }}</span
          >
        </div>
      </div>
      <div :class="['w-4 h-4 rounded-full mt-1 flex-shrink-0', dog.erfahrung]" />
    </div>

    <p v-if="fehler" class="text-sm text-red-700">{{ fehler }}</p>
    <p
      v-else-if="filteredDogs.length === 0 && gefilterteTeilfenster.length === 0"
      class="text-sm text-gray-500 italic"
    >
      Keine freien Hunde gefunden.
    </p>
    <p v-else-if="filteredDogs.length === 0" class="text-sm text-gray-500 italic">
      Kein Hund ist für den ganzen Zeitraum frei. Unten findest du Hunde, die zeitweise frei sind.
    </p>

    <!-- ZEITWEISE FREIE HUNDE -->
    <template v-if="gefilterteTeilfenster.length > 0">
      <h2 class="text-xl font-bold">Zeitweise frei</h2>
      <div
        v-for="fenster in gefilterteTeilfenster"
        :key="fenster.von + fenster.bis"
        class="space-y-3"
      >
        <h3 class="text-sm font-bold text-gray-700 uppercase tracking-wide">
          {{ formatiereUhrzeit(fenster.von) }} – {{ formatiereUhrzeit(fenster.bis) }} Uhr
        </h3>

        <div
          v-for="dog in fenster.hunde"
          :key="dog.id"
          @click="selectDog(dog, fenster)"
          class="bg-[#BFCABF] p-4 rounded-xl cursor-pointer flex gap-4 hover:bg-[#b0bcaf] transition-colors relative"
        >
          <img
            v-if="dog.image"
            :src="dog.image"
            :alt="dog.name"
            class="w-24 h-24 rounded-lg object-cover pointer-events-none flex-shrink-0"
          />
          <div
            v-else
            class="w-24 h-24 rounded-lg bg-[#94A684]/40 flex items-center justify-center text-3xl flex-shrink-0"
          >
            🐾
          </div>
          <div class="flex-grow">
            <h3 class="text-lg font-bold">{{ dog.name }}</h3>
            <p class="text-sm">{{ dog.breed }} • {{ dog.age }} Jahre, {{ dog.gender }}</p>
            <div class="flex gap-2 mt-2 flex-wrap">
              <span
                v-for="tag in dog.tags"
                :key="tag.id"
                :class="tag.hinweis ? 'bg-orange-500/50' : 'bg-white/50'"
                class="px-2 py-0.5 rounded-full text-xs"
                >{{ tag.name }}</span
              >
            </div>
          </div>
          <div :class="['w-4 h-4 rounded-full mt-1 flex-shrink-0', dog.erfahrung]" />
        </div>
      </div>
    </template>

    <!-- HILFE BUTTON -->
    <button
      class="fixed bottom-20 right-8 bg-gray-600 text-white w-12 h-12 rounded-full font-bold text-xl shadow-lg flex items-center justify-center"
    >
      ?
    </button>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { formatiereUhrzeit } from '@/components/utils/datum.js'

const router = useRouter()
const route = useRoute()

const searchQuery = ref('')
const terminData = ref(null)

onMounted(async () => {
  const data = localStorage.getItem('terminData')
  if (data) {
    terminData.value = JSON.parse(data)
    await ladeVerfuegbareHunde()
  } else {
    redirectToTimeSelection()
  }
})

const redirectToTimeSelection = () => {
  const currentPath = route?.path || window.location.pathname
  if (currentPath.startsWith('/app/admin')) {
    router.push('/app/admin/terminMitg')
  } else {
    router.push('/app/reservierung/zeitwahl')
  }
}

const allDogs = ref([])
const teilFenster = ref([])
const vorschlagHund = ref(null)
const fehler = ref('')

const mappeHund = (h) => ({
  id: h.id,
  name: h.name,
  breed: h.rasse,
  age: h.jahre,
  gender: h.geschlecht ? 'Hündin' : 'Rüde',
  tags: (h.labels ?? []).map((l) => ({ id: l.id, name: l.bezeichnung, hinweis: l.hinweis })),
  erfahrung: h.erfahrung ? 'bg-orange-500' : 'bg-green-500',
  image: h.bild,
})

const ladeVerfuegbareHunde = async () => {
  try {
    if (!terminData.value) {
      redirectToTimeSelection()
      return
    }

    const { mitgliedId, datum, von, bis } = terminData.value

    if (!mitgliedId || !datum || !von || !bis) {
      redirectToTimeSelection()
      return
    }

    const res = await fetch(
      `/api/hund/all/available/${mitgliedId}?datum=${datum}&von=${von}&bis=${bis}`,
    )
    if (res.ok) {
      const data = await res.json()

      allDogs.value = (data.verfuegbareHunde ?? []).map(mappeHund)
      vorschlagHund.value = data.vorschlag ? mappeHund(data.vorschlag) : null
      // nur zeitweise freie Hunde, gruppiert nach Teilzeitraum
      teilFenster.value = (data.teilweise ?? []).map((f) => ({
        von: f.von,
        bis: f.bis,
        hunde: (f.hunde ?? []).map(mappeHund),
      }))
    } else {
      fehler.value = await res.text()
    }
  } catch (e) {
    console.error('Netzwerkfehler beim Laden der Hunde:', e)
  }
}

const sucheTreffer = (dog) => dog.name.toLowerCase().includes(searchQuery.value.toLowerCase())
const filteredDogs = computed(() => {
  return allDogs.value.filter(sucheTreffer)
})

// Suche gilt auch für die Teilgruppen, leere Gruppen verschwinden
const gefilterteTeilfenster = computed(() =>
  teilFenster.value
    .map((f) => ({ ...f, hunde: f.hunde.filter(sucheTreffer) }))
    .filter((f) => f.hunde.length > 0),
)

// fenster ist nur bei einem zeitweise freien Hund gesetzt
const selectDog = (dog, fenster = null) => {
  const gekuerzterZeitraum = fenster
    ? {
        von: fenster.von,
        bis: fenster.bis,
        start: fenster.von,
        end: fenster.bis,
        zeiten: undefined,
      }
    : {}

  localStorage.setItem(
    'bookingFinal',
    JSON.stringify({
      ...terminData.value,
      ...gekuerzterZeitraum,
      dog: dog,
    }),
  )

  const currentPath = route?.path || window.location.pathname

  if (currentPath.startsWith('/app/admin')) {
    router.push('/app/admin/ueberpruefen')
  } else {
    router.push('/app/reservierung/ueberpruefen')
  }
}
</script>
