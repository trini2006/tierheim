<template>
  <div class="max-w-md mx-auto p-4 space-y-6 pb-24">
    <!-- RANDOM HUND VORSCHLAG -->
    <h2 class="text-xl font-bold">Unser Vorschlag</h2>
    <div v-if="randomDog" @click="selectDog(randomDog)" class="bg-[#BFCABF] p-4 rounded-xl cursor-pointer hover:bg-[#b0bcaf] transition-colors relative">
      <div class="flex gap-4">
        <img :src="randomDog.image || 'https://via.placeholder.com/150'" class="w-24 h-24 rounded-lg object-cover pointer-events-none" />
        <div class="flex-grow">
          <div class="flex justify-between items-start">
            <h3 class="text-lg font-bold">{{ randomDog.name }}</h3>
            <div :class="['w-4 h-4 rounded-full', randomDog.color]" />
          </div>
          <p class="text-sm">{{ randomDog.breed }} • {{ randomDog.age }} Jahre, {{ randomDog.gender }}</p>
          <div class="flex gap-2 mt-2 flex-wrap">
            <span v-for="tag in randomDog.tags" :key="tag" class="bg-white/50 px-2 py-0.5 rounded-full text-xs">{{ tag }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- SUCHE -->
    <div class="relative">
      <input v-model="searchQuery" type="text" placeholder="Suche nach Name..." 
             class="w-full p-3 rounded-full bg-gray-200 pl-10" />
    </div>

    <!-- LISTE FREIER HUNDE -->
    <h2 class="text-xl font-bold">Freie Hunde</h2>
    <div v-for="dog in filteredDogs" :key="dog.id" @click="selectDog(dog)" 
         class="bg-[#BFCABF] p-4 rounded-xl cursor-pointer flex gap-4 hover:bg-[#b0bcaf] transition-colors relative">
      <img :src="dog.image || 'https://via.placeholder.com/150'" class="w-24 h-24 rounded-lg object-cover pointer-events-none flex-shrink-0" />
      <div class="flex-grow">
        <h3 class="text-lg font-bold">{{ dog.name }}</h3>
        <p class="text-sm">{{ dog.breed }} • {{ dog.age }} Jahre, {{ dog.gender }}</p>
        <div class="flex gap-2 mt-2 flex-wrap">
          <span v-for="tag in dog.tags" :key="tag" class="bg-white/50 px-2 py-0.5 rounded-full text-xs">{{ tag }}</span>
        </div>
      </div>
      <div :class="['w-4 h-4 rounded-full mt-1 flex-shrink-0', dog.color]" />
    </div>

    <div v-if="filteredDogs.length === 0" class="text-sm text-gray-500 italic">
      Keine freien Hunde gefunden.
    </div>

    <!-- HILFE BUTTON -->
    <button class="fixed bottom-20 right-8 bg-gray-600 text-white w-12 h-12 rounded-full font-bold text-xl shadow-lg flex items-center justify-center">?</button>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

const searchQuery = ref('')
const terminData = ref(null)

// Hier ist der Mock-Hund als Standard-Fallback definiert
const mockHund = {
  id: 999,
  name: 'Wambo (Beispiel)',
  breed: 'Husky-Chow Mix',
  age: 3,
  gender: 'Männlich',
  tags: ['Freundlich', 'Verspielt'],
  color: 'bg-green-500',
  image: ''
}

const allDogs = ref([mockHund])

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
    router.push('/app/admin/zeitwahl')
  } else {
    router.push('/app/reservierung/zeitwahl')
  }
}

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

    const res = await fetch(`/hund/all/available/${mitgliedId}?datum=${datum}&von=${von}&bis=${bis}`)
    if (res.ok) {
      const data = await res.json()
      if (data && data.length > 0) {
        allDogs.value = data.map(h => ({
          id: h.id,
          name: h.name,
          breed: h.rasse,
          age: h.jahre,
          gender: h.geschlecht,
          tags: h.labels ? h.labels.map(l => l.name) : [],
          color: h.erfahrung ? 'bg-orange-500' : 'bg-green-500',
          image: h.bildUrl
        }))
      }
    } else {
      console.error('Fehler beim Laden der Hunde, Status:', res.status)
    }
  } catch (e) {
    console.error('Netzwerkfehler beim Laden der Hunde, behalte Mock-Hund:', e)
  }
}

const filteredDogs = computed(() => {
  return allDogs.value.filter(dog => 
    dog.name.toLowerCase().includes(searchQuery.value.toLowerCase())
  )
})

const randomDog = computed(() => {
  if (allDogs.value.length === 0) return null
  return allDogs.value[Math.floor(Math.random() * allDogs.value.length)]
})

const selectDog = (dog) => {
  localStorage.setItem('bookingFinal', JSON.stringify({
    ...terminData.value,
    dog: dog
  }))

  const currentPath = route?.path || window.location.pathname

  if (currentPath.startsWith('/app/admin')) {
    router.push('/app/admin/ueberpruefen')
  } else {
    router.push('/app/reservierung/ueberpruefen')
  }
}
</script>