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
    // 1. VERSUCH: Admin-Login prüfen
    const adminResponse = await fetch('http://localhost:8085/admin/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(loginDaten)
    })

    if (adminResponse.ok) {
      const isAdmingueltig = await adminResponse.json()
      if (isAdmingueltig) {
        localStorage.setItem('userRole', 'admin')
        localStorage.setItem('userName', benutzername.value)
        router.push('/app/admin')
        return
      }
    }

    // 2. VERSUCH: Mitglied-Login prüfen (falls kein Admin)
    const mitgliedResponse = await fetch('http://localhost:8085/mitglied/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
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

    // 3. KEIN TREFFER in beiden Endpunkten
    fehlermeldung.value = 'Ungültiger Benutzername oder falsches Passwort.'

  } catch (error) {
    console.error('Verbindungsfehler zum Backend:', error)
    fehlermeldung.value = 'Serverfehler beim Verbindungsaufbau.'
  }
}
</script>