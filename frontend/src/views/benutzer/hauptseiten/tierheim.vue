<template>
  <div
    class="w-full bg-white rounded-3xl shadow-sm border border-gray-100 p-6 md:p-12 min-h-[70vh]"
  >
    <!-- Mobile Logo: Zentriert -->
    <div class="flex md:hidden justify-center mb-8">
      <div
        class="w-24 h-24 bg-white rounded-full p-2 shadow-md flex items-center justify-center border border-gray-200"
      >
        <div
          class="w-full h-full rounded-full flex items-center justify-center bg-gray-50 text-center p-2"
        >
          <span class="text-xs font-bold text-gray-500 leading-tight">
            Tierheim<br />Weiden Logo
          </span>
        </div>
      </div>
    </div>

    <!-- ZWEISPALTIGES RASTER -->
    <div class="grid grid-cols-1 md:grid-cols-2 gap-10 lg:gap-16 items-start">
      <!-- LINKE SPALTE: Kontaktdaten & Gassi-Zeiten -->
      <div class="space-y-10 flex flex-col items-center md:items-start text-center md:text-left">
        <!-- Kontaktdaten -->
        <div class="w-full">
          <h3 class="text-xl font-bold text-gray-900 mb-4 font-sans">Kontaktdaten</h3>
          <div v-if="tierheim" class="space-y-1 text-sm md:text-base text-gray-700">
            <p class="font-medium">{{ tierheim.strasse }} {{ tierheim.hausnummer }}</p>
            <p class="font-medium">{{ tierheim.plz }} {{ tierheim.ort }}</p>
            <p class="pt-2"> {{ tierheim.telNummer }}</p>
            <p v-if="tierheim.notNummer"> {{ tierheim.notNummer }} (Notfall)</p>
            <p class="pt-2">
              <a v-bind:href="[tierheim.link]" target="_blank" class="hover:underline text-emerald-800 font-semibold">
                {{ tierheim.showLink }}
              </a>
            </p>
          </div>
        </div>

        <!-- Gassi Zeiten: Jetzt direkt unter den Kontaktdaten -->
        <div class="w-full">
          <h3 class="text-xl font-bold text-gray-900 mb-4 font-sans">Gassi Zeiten</h3>

          <!-- Hier wurde die Ausrichtung auf md:justify-start geändert -->
          <div class="flex justify-center md:justify-start">
            <div class="text-sm md:text-base text-gray-700 space-y-0.5">
              <div v-for="res in alleZeiten" :key="res.id" class="flex justify-start gap-4">
                <span class="font-bold w-6"> {{ res.tag }}</span>
                <span class="whitespace-nowrap">
                  {{ formatiereUhrzeit(res.von) }} - {{ formatiereUhrzeit(res.bis) }} Uhr</span
                >
              </div>
            </div>
          </div>
        </div>

        <!-- Button -->
        <RouterLink
          to="/app/reservierung/zeitwahl"
          class="bg-[#536B53] text-white px-6 py-2 rounded-lg hover:bg-[#3d4f3d] transition"
        >
          zu den Terminen
        </RouterLink>
      </div>

      <!-- RECHTE SPALTE: Unsere Hunde -->
      <div class="space-y-6 w-full flex flex-col items-center md:items-start">
        <div class="flex items-center gap-3">
          <span class="text-xl">🐕</span>
          <h3 class="text-xl font-bold text-gray-800 font-sans">Unsere Hunde</h3>
        </div>

        <div class="space-y-3.5 w-full max-w-sm">
          <div
            v-for="hund in alleHunde"
            :key="hund.id"
            @click="viewDogDetails(hund.id)"
            class="bg-[#E3E9DD] hover:bg-[#d6decb] rounded-full p-2 pl-3 pr-6 flex items-center justify-between cursor-pointer shadow-sm transition-all hover:translate-x-1"
          >
            <div class="flex items-center gap-4">
              <div
                class="w-11 h-11 rounded-full bg-white flex items-center justify-center border border-gray-200"
              >
                <span class="text-xl">🐶</span>
              </div>
              <span class="font-bold text-gray-800 text-sm md:text-base">{{ hund.name }}</span>
            </div>
            <svg
              xmlns="http://www.w3.org/2000/svg"
              class="w-4 h-4 text-gray-600"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
              stroke-width="3"
            >
              <path stroke-linecap="round" stroke-linejoin="round" d="M9 5l7 7-7 7" />
            </svg>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { formatiereUhrzeit } from '@/components/utils/datum.js'

const router = useRouter()

const alleZeiten = ref([])
const ladeZeiten = async () => {
  try {
    const res = await fetch('/api/gassiGehZeiten/all')
    if (res.ok) {
      alleZeiten.value = await res.json()
    }
  } catch (e) {
    console.error('Fehler beim Laden der Gassi-Geh-Zeiten:', e)
  }
}

const tierheimId = 1
const alleHunde = ref([])
const ladeHunde = async () => {
  try {
    const response = await fetch(`/api/hund/${tierheimId}/all`)
    if (response.ok) {
      alleHunde.value = await response.json()
    } else {
      console.error('Fehler beim Laden der Hunde')
    }
  } catch (error) {
    console.error('Verbindungsfehler zum Backend:', error)
  }
}

const tierheim = ref([])
const ladeTierheimDaten = async () => {
  try {
    const response = await fetch(`/api/tierheim/${tierheimId}`)
    if (response.ok) {
      const data = await response.json()
      tierheim.value = {
        name: data.name,
        strasse: data.strasse,
        hausnummer: data.hausnummer,
        plz: data.plz,
        ort: data.ort,
        telNummer: data.telefonnummer,
        notNummer: data.notfallnummer ? data.notfallnummer : '',
        email: data.email,
        link: data.link ? data.link : '',
        showLink: data.link ? data.link.split('/').at(2) : ''
      }
    } else {
      console.error('Fehler beim Laden des Tierheims')
    }
  } catch (error) {
    console.error('Verbindungsfehler zum Backend:', error)
  }
}

onMounted(() => {
  ;(ladeZeiten(), ladeHunde(), ladeTierheimDaten())
})

const goToReservations = () => router.push('/app')
const viewDogDetails = (id) => router.push(`/app/hund/${id}`)
</script>
