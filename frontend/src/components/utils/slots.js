export const SLOT_MINUTEN = 30

export const minutenVon = (zeit) => {
  const [h, m] = zeit.split(':')
  return Number(h) * 60 + Number(m)
}

export const alsText = (min) =>
  `${String(Math.floor(min / 60)).padStart(2, '0')}:${String(min % 60).padStart(2, '0')}`
