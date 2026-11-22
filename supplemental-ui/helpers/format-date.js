'use strict'

const FORMATTER = new Intl.DateTimeFormat('en-US', {
  year: 'numeric',
  month: 'short',
  day: 'numeric',
  timeZone: 'UTC',
})

module.exports = (isoDate) => {
  if (!isoDate || typeof isoDate !== 'string') return ''
  const parsed = new Date(`${isoDate.trim()}T00:00:00Z`)
  return Number.isNaN(parsed.getTime()) ? isoDate : FORMATTER.format(parsed)
}
