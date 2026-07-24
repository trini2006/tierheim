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
  
  const loginPruefen = () => {
    // Beispiel-Datenbanktabellen (in deiner echten App kommen diese Daten aus deiner DB)
    const adminTabelle = [
      { username: 'admin', passwort: 'geheim' }
    ]
    
    const benutzerTabelle = [
      { username: 'user1', passwort: '123456' },
      { username: 'user2', passwort: 'password' }
    ]
  
    // 1. VERSUCH: In der Admin-Tabelle suchen
    const gefundenerAdmin = adminTabelle.find(
      a => a.username === benutzername.value && a.passwort === passwort.value
    )
  
    if (gefundenerAdmin) {
      localStorage.setItem('userRole', 'admin')
      localStorage.setItem('userName', gefundenerAdmin.username)
      router.push('/app/admin')
      return
    }
  
    // 2. VERSUCH: In der Benutzer-Tabelle suchen (da kein Admin gefunden wurde)
    const gefundenerBenutzer = benutzerTabelle.find(
      b => b.username === benutzername.value && b.passwort === passwort.value
    )
  
    if (gefundenerBenutzer) {
      localStorage.setItem('userRole', 'user')
      localStorage.setItem('userName', gefundenerBenutzer.username)
      router.push('/app')
      return
    }
  
    // 3. KEIN TREFFER in beiden Tabellen
    fehlermeldung.value = 'Ungültiger Benutzername oder falsches Passwort.'
  }
  </script>