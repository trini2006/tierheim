import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useReservierungenStore = defineStore('reservierungen', () => {
  const alle = ref([])
  const geladen = ref(false)
  const fehler = ref(null)
  const ausgewaehltId = ref(null) // NEU

  const aktive = computed(() => alle.value.filter((r) => r.status === 'AKTIV'))
  const naechste = computed(() => aktive.value[0] ?? null)

  // gewählter Termin, sonst der nächste
  const angezeigte = computed(
    () => alle.value.find((r) => r.id === ausgewaehltId.value) ?? naechste.value,
  )
  const waehle = (id) => {
    ausgewaehltId.value = id
  }

  const reset = () => {
    alle.value = []
    geladen.value = false
    fehler.value = null
    ausgewaehltId.value = null
  }

  const lade = async () => {
    let mitgliedId = null
    try {
      mitgliedId = JSON.parse(localStorage.getItem('userId'))
    } catch {
      /* ungültiger Wert -> wie "nicht eingeloggt" behandeln */
    }
    if (!mitgliedId) {
      reset()
      return
    }

    const lokal = (d) => d.toLocaleDateString('sv-SE')
    // maximal 14 Tage im voraus
    const bis = new Date()
    bis.setDate(bis.getDate() + 14)
    try {
      const res = await fetch(
        `/api/reservierung/all?von=${lokal(new Date())}&bis=${lokal(bis)}&mitgliedId=${mitgliedId}`,
      )
      if (!res.ok) throw new Error(`HTTP ${res.status}`)
      const jetzt = new Date()
      alle.value = (await res.json())
        .filter((r) => new Date(`${r.datum}T${r.zeitBis}`) > jetzt)
        .sort((a, b) => `${a.datum}T${a.zeitAb}`.localeCompare(`${b.datum}T${b.zeitAb}`))
      // Auswahl verwerfen, wenn der Termin nicht mehr existiert
      if (!alle.value.some((r) => r.id === ausgewaehltId.value)) ausgewaehltId.value = null
      fehler.value = null
      geladen.value = true
    } catch (e) {
      console.error('Fehler beim Laden der Reservierungen:', e)
      fehler.value = 'Reservierungen konnten nicht geladen werden.'
    }
  }

  const ladeFallsNoetig = () => (geladen.value ? Promise.resolve() : lade())

  const stornieren = async (id, grund = 'Storniert durch Mitglied') => {
    const saubererGrund = String(grund || '').trim()
    if (!saubererGrund) {
      throw new Error('Bitte gib einen Grund für die Stornierung an.')
    }

    const res = await fetch(`/api/reservierung/${id}?grund=${encodeURIComponent(saubererGrund)}`, {
      method: 'DELETE',
    })
    if (!res.ok) throw new Error(await res.text())
    await lade()
  }

  return {
    alle,
    aktive,
    naechste,
    angezeigte,
    ausgewaehltId,
    geladen,
    fehler,
    lade,
    ladeFallsNoetig,
    stornieren,
    waehle,
    reset,
  }
})
