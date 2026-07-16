<template>
  <div class="max-w-md mx-auto p-4 space-y-6 pb-24">
    <!-- RANDOM HUND VORSCHLAG -->
    <h2 class="text-xl font-bold">Unser Vorschlag</h2>
    <div v-if="randomDog" @click="selectDog(randomDog)" class="bg-[#BFCABF] p-4 rounded-xl cursor-pointer">
      <div class="flex gap-4">
        <img :src="randomDog.image" class="w-24 h-24 rounded-lg object-cover" />
        <div>
          <h3 class="text-lg font-bold">{{ randomDog.name }}</h3>
          <p class="text-sm">{{ randomDog.breed }} • {{ randomDog.age }} Jahre, {{ randomDog.gender }}</p>
          <div class="flex gap-2 mt-2">
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
         class="bg-[#BFCABF] p-4 rounded-xl cursor-pointer flex gap-4">
      <img :src="dog.image" class="w-24 h-24 rounded-lg object-cover" />
      <div class="flex-grow">
        <h3 class="text-lg font-bold">{{ dog.name }}</h3>
        <p class="text-sm">{{ dog.breed }} • {{ dog.age }} Jahre, {{ dog.gender }}</p>
        <div class="flex gap-2 mt-2">
          <span v-for="tag in dog.tags" :key="tag" class="bg-white/50 px-2 py-0.5 rounded-full text-xs">{{ tag }}</span>
        </div>
      </div>
      <div :class="['w-4 h-4 rounded-full mt-2', dog.color]" />
    </div>

    <!-- HILFE BUTTON -->
    <button class="fixed bottom-20 right-8 bg-gray-600 text-white w-12 h-12 rounded-full font-bold text-xl shadow-lg">?</button>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const searchQuery = ref('')
const terminData = ref(null)

// Beispiel-Datenbank
const allDogs = [
  { id: 1, name: 'Dark', breed: 'Mischling', age: 7, gender: 'Rüde', tags: ['ruhig', 'zurückhaltend'], color: 'bg-orange-500', image: 'https://via.placeholder.com/150' },
  { id: 2, name: 'Richy', breed: 'Labrador', age: 4, gender: 'Rüde', tags: ['freundlich', 'Kinder'], color: 'bg-green-500', image: 'https://via.placeholder.com/150' },
  { id: 3, name: 'Xina', breed: 'Bully-Mix', age: 6, gender: 'Hündin', tags: ['freundlich', 'Leinenzug'], color: 'bg-orange-500', image: 'https://via.placeholder.com/150' }
]

onMounted(() => {
  const data = localStorage.getItem('terminData')
  if (data) terminData.value = JSON.parse(data)
})

// Logik: Hier würden wir normalerweise gegen ein Backend filtern
const filteredDogs = computed(() => {
  return allDogs.filter(dog => 
    dog.name.toLowerCase().includes(searchQuery.value.toLowerCase())
  )
})

const randomDog = computed(() => allDogs[Math.floor(Math.random() * allDogs.length)])

const selectDog = (dog) => {
  // Übergibt den Hund zusammen mit den Zeit-Daten an die nächste Seite
  localStorage.setItem('bookingFinal', JSON.stringify({
    ...terminData.value,
    dog: dog
  }))
  router.push('/reservierung/ueberpruefen')
}
</script>