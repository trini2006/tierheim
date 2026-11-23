import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { useToast } from '@/composables/useToast.js'

const TIERHEIM_ID = 1 // ToDo: aus dem Login ableiten

// Wessen Posteingang? Admin -> Tierheim, sonst das angemeldete Mitglied
const leseEmpfaenger = () => {
  const istAdmin = (localStorage.getItem('userRole') ?? '').toLowerCase() === 'admin'
  if (istAdmin) return `tierheim/${TIERHEIM_ID}`
  let id = null
  try {
    id = JSON.parse(localStorage.getItem('userId'))
  } catch {
    /* nicht eingeloggt */
  }
  return id ? `mitglied/${id}` : null
}

export const useBenachrichtigungenStore = defineStore('benachrichtigungen', () => {
  const alle = ref([])
  const geladen = ref(false)
  const fehler = ref(null)

  const ungelesen = computed(() => alle.value.filter((n) => !n.gelesen).length)
  // das Backend liefert neueste zuerst
  const neuesteUngelesene = computed(() => alle.value.find((n) => !n.gelesen) ?? null)

  const toast = useToast()
  const bekannteIds = new Set()

  const reset = () => {
    alle.value = []
    geladen.value = false
    fehler.value = null
    bekannteIds.clear()
  }

  const lade = async () => {
    const empfaenger = leseEmpfaenger()
    if (!empfaenger) {
      reset()
      return
    }
    try {
      const res = await fetch(`/api/benachrichtigung/${empfaenger}`)
      if (!res.ok) throw new Error(`HTTP ${res.status}`)
      const daten = await res.json()

      const neu = daten.filter((n) => !n.gelesen && !bekannteIds.has(n.id))
      daten.forEach((n) => bekannteIds.add(n.id))
      // nicht beim ersten Laden (da zeigt schon der Zähler, was offen ist)
      if (geladen.value && neu.length > 0) {
        toast.info(
          neu.length === 1 ? `Neue Nachricht: ${neu[0].text}` : `${neu.length} neue Nachrichten`,
          8000,
        )
      }

      alle.value = daten
      fehler.value = null
      geladen.value = true
    } catch (e) {
      console.error('Fehler beim Laden der Nachrichten:', e)
      fehler.value = 'Nachrichten konnten nicht geladen werden.'
    }
  }

  const markiereGelesen = async (id) => {
    const res = await fetch(`/api/benachrichtigung/${id}/gelesen`, { method: 'PUT' })
    if (!res.ok) return
    const eintrag = alle.value.find((n) => n.id === id)
    if (eintrag) eintrag.gelesen = true
  }

  const markiereAlleGelesen = () =>
    Promise.all(alle.value.filter((n) => !n.gelesen).map((n) => markiereGelesen(n.id)))

  // regelmäßig nachladen, solange eine Seite mit Badge offen ist
  let timer = null
  const stoppeAbfrage = () => {
    if (timer) {
      clearInterval(timer)
      timer = null
    }
  }
  const starteAbfrage = () => {
    stoppeAbfrage()
    lade()
    timer = setInterval(() => {
      if (document.visibilityState === 'visible') lade()
    }, 60000)
  }

  return {
    alle,
    geladen,
    fehler,
    ungelesen,
    neuesteUngelesene,
    lade,
    markiereGelesen,
    markiereAlleGelesen,
    starteAbfrage,
    stoppeAbfrage,
    reset,
  }
})
