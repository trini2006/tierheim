<template>
  <div class="max-w-md mx-auto mt-20 p-8 bg-white rounded-3xl shadow-sm border border-gray-100 space-y-6">
    <h2 class="text-2xl font-bold text-gray-800 text-center">Anmeldung</h2>
 
    <form @submit.prevent="loginPruefen" class="space-y-4">
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">Benutzername</label>
        <input 
          v-model="benutzername" 
          type="text" 
          required 
          class="w-full px-4 py-2 rounded-xl bg-[#D3DDD1]/40 border border-gray-200 focus:outline-none focus:border-[#8FA18C]"
          placeholder="Dein Name"
        />
      </div>
 
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">Passwort</label>
        <input 
          v-model="passwort" 
          type="password" 
          required 
          class="w-full px-4 py-2 rounded-xl bg-[#D3DDD1]/40 border border-gray-200 focus:outline-none focus:border-[#8FA18C]"
          placeholder="••••••••"
        />
      </div>
 
      <p v-if="fehlermeldung" class="text-sm text-red-600 text-center">{{ fehlermeldung }}</p>
 
      <button 
        type="submit" 
        class="w-full py-3 bg-[#8FA18C] hover:bg-[#7e917b] text-white font-bold rounded-xl shadow transition-colors"
      >
        Einloggen
      </button>
    </form>
  </div>
</template>
 
<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
 
const router = useRouter()
const benutzername = ref('')
const passwort = ref('')
const fehlermeldung = ref('')
 
const loginPruefen = async () => {
  fehlermeldung.value = ''

  const loginDaten = {
    username: benutzername.value,
    passwort: passwort.value
  }

  try {
    // 1. VERSUCH: Admin-Login prüfen (Port 8085)
    const adminResponse = await fetch('http://localhost:8085/admin/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(loginDaten)
    })

    if (adminResponse.ok) {
      const isAdminGueltig = await adminResponse.json()
      if (isAdminGueltig) {
        localStorage.setItem('userRole', 'admin')
        localStorage.setItem('userName', benutzername.value)
        router.push('/app/admin')
        return
      }
    }

    // 2. VERSUCH: Mitglied-Login prüfen
    const mitgliedResponse = await fetch('http://localhost:8085/mitglied/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(loginDaten)
    })

    if (mitgliedResponse.ok) {
      const isMitgliedGueltig = await mitgliedResponse.json()
      if (isMitgliedGueltig) {
        localStorage.setItem('userRole', 'user')
        localStorage.setItem('userName', benutzername.value)
        router.push('/app')
        return
      }
    }

    // 3. KEIN TREFFER
    fehlermeldung.value = 'Ungültiger Benutzername oder falsches Passwort.'

  } catch (error) {
    console.error('Verbindungsfehler zum Backend:', error)
    fehlermeldung.value = 'Serverfehler beim Verbindungsaufbau.'
  }
}
</script>