export const SLOT_MINUTEN = 30

export const minutenVon = (zeit) => {
  const [h, m] = zeit.split(':')
  return Number(h) * 60 + Number(m)
}

export const alsText = (min) =>
  `${String(Math.floor(min / 60)).padStart(2, '0')}:${String(min % 60).padStart(2, '0')}`

export function erzeugeSlots(fenster) {
  const slots = []
  fenster
    .slice()
    .sort((a, b) => a.von.localeCompare(b.von))
    .forEach((g, fensterIndex) => {
      const ende = minutenVon(g.bis)
      for (let start = minutenVon(g.von); start + SLOT_MINUTEN <= ende; start += SLOT_MINUTEN) {
        const von = alsText(start)
        const bis = alsText(start + SLOT_MINUTEN)
        slots.push({
          id: slots.length + 1,
          fenster: fensterIndex,
          von, // "08:00"
          bis, // "08:30"
          vonMin: start,
          bisMin: start + SLOT_MINUTEN,
          label: `${von} - ${bis}`,
        })
      }
    })
  return slots
}
