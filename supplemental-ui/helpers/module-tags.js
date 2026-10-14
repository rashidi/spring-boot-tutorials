'use strict'

const STOP_WORDS = new Set(['spring', 'boot', 'tutorial', 'example', 'with'])

module.exports = (url) => {
  if (!url || typeof url !== 'string') return []
  const match = url.match(/\/([^/]+)\.html$/)
  if (!match || match[1] === 'index') return []
  return match[1]
    .split('-')
    .filter((part) => part && !STOP_WORDS.has(part.toLowerCase()))
    .slice(0, 4)
}
