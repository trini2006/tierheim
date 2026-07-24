<template>
  <div class="max-w-md mx-auto p-4 space-y-6 pb-24">
    <!-- RANDOM HUND VORSCHLAG -->
<h2 class="text-xl font-bold">Unser Vorschlag</h2>
<div v-if="randomDog" @click="selectDog(randomDog)" class="bg-[#BFCABF] p-4 rounded-xl cursor-pointer hover:bg-[#b0bcaf] transition-colors">
  <div class="flex gap-4">
    <img :src="randomDog.image" class="w-24 h-24 rounded-lg object-cover pointer-events-none" />
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
     class="bg-[#BFCABF] p-4 rounded-xl cursor-pointer flex gap-4 hover:bg-[#b0bcaf] transition-colors">
  <img :src="dog.image" class="w-24 h-24 rounded-lg object-cover pointer-events-none" />
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
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

const searchQuery = ref('')
const terminData = ref(null)

const allDogs = [
  { id: 1, name: 'Dark', breed: 'Mischling', age: 7, gender: 'Rüde', tags: ['ruhig', 'zurückhaltend'], color: 'bg-orange-500', image: 'https://images.unsplash.com/photo-1543466835-00a7907e9de1' },
  { id: 2, name: 'Richy', breed: 'Labrador', age: 4, gender: 'Rüde', tags: ['freundlich', 'Kinder'], color: 'bg-green-500', image: 'https://images.unsplash.com/photo-1552053831-71594a27632d' },
  { id: 3, name: 'Xina', breed: 'Bully-Mix', age: 6, gender: 'Hündin', tags: ['freundlich', 'Leinenzug'], color: 'bg-orange-500', image: 'https://images.unsplash.com/photo-1583511655857-d19b40a7a54e' }
]

onMounted(() => {
  const data = localStorage.getItem('terminData')
  if (data) terminData.value = JSON.parse(data)
})

const filteredDogs = computed(() => {
  return allDogs.filter(dog => 
    dog.name.toLowerCase().includes(searchQuery.value.toLowerCase())
  )
})

const randomDog = computed(() => allDogs[Math.floor(Math.random() * allDogs.length)])

const selectDog = (dog) => {
  localStorage.setItem('bookingFinal', JSON.stringify({
    ...terminData.value,
    dog: dog
  }))

  // Sicherheits-Check, ob route und path existieren
  const currentPath = route?.path || window.location.pathname

  if (currentPath.startsWith('/app/admin')) {
    router.push('/app/admin/ueberpruefen')
  } else {
    router.push('/app/reservierung/ueberpruefen')
  }
}
</script>