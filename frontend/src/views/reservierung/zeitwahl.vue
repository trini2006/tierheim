<template>
  <div class="max-w-md md:max-w-3xl mx-auto p-4 md:p-8 space-y-6">
    <ZeitAuswahl heute-sperren :mitglied-id="mitgliedId" @change="auswahl = $event" />

    <div class="flex justify-end">
      <button
        @click="weiter"
        :disabled="!auswahl"
        class="flex items-center gap-3 px-8 py-3 rounded-full shadow-md font-bold transition-all"
        :class="
          !auswahl
            ? 'bg-gray-300 text-gray-500 cursor-not-allowed'
            : 'bg-[#D3DDD1] hover:bg-[#c2cebf] text-gray-800 border border-gray-300'
        "
      >
        <span>Weiter</span>
        <div
          class="w-8 h-8 rounded-full bg-emerald-600 flex items-center justify-center text-white"
        >
          ✓
        </div>
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import ZeitAuswahl from '@/components/utils/ZeitAuswahl.vue'

const router = useRouter()
const auswahl = ref(null)

const leseMitgliedId = () => {
  try {
    return JSON.parse(localStorage.getItem('userId'))
  } catch {
    return null
  }
}
const mitgliedId = leseMitgliedId()

function weiter() {
  if (!auswahl.value) return
  if (!mitgliedId) {
    router.push('/')
    return
  } // nicht eingeloggt
  localStorage.setItem('terminData', JSON.stringify({ mitgliedId, ...auswahl.value }))
  router.push('/app/reservierung/hundewahl')
}
</script>
