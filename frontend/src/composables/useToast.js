import { reactive } from 'vue'

// Modul-Ebene: Alle Aufrufer teilen sich dieselbe Liste
const toasts = reactive([])
let naechsteId = 1
const MAX_SICHTBAR = 4

const entferne = (id) => {
  const i = toasts.findIndex((t) => t.id === id)
  if (i !== -1) toasts.splice(i, 1)
}

const zeige = (text, art, dauer) => {
  const id = naechsteId++
  toasts.push({ id, text, art })
  if (toasts.length > MAX_SICHTBAR) toasts.shift()
  if (dauer > 0) setTimeout(() => entferne(id), dauer)
  return id
}

export function useToast() {
  return {
    toasts,
    entferne,
    info: (text, dauer = 4000) => zeige(text, 'info', dauer),
    erfolg: (text, dauer = 4000) => zeige(text, 'erfolg', dauer),
    fehler: (text, dauer = 6000) => zeige(text, 'fehler', dauer),
  }
}
