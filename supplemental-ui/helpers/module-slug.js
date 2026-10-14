'use strict'

module.exports = (url) => {
  if (!url || typeof url !== 'string') return ''
  const match = url.match(/\/([^/]+)\.html$/)
  return match && match[1] !== 'index' ? match[1] : ''
}
