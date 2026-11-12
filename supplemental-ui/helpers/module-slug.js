'use strict'

const PREFIXED_CATEGORIES = new Set([
  'ai',
  'batch',
  'data',
  'messaging',
  'modulith',
  'security',
  'test',
  'web',
])

module.exports = (url) => {
  if (!url || typeof url !== 'string') return null
  const match = url.match(/\/([^/]+)\.html$/)
  if (!match || match[1] === 'index') return null

  const slug = match[1]
  let path
  if (slug === 'modulith') {
    path = 'modulith/core'
  } else {
    const dashIndex = slug.indexOf('-')
    const prefix = dashIndex > 0 ? slug.slice(0, dashIndex) : ''
    if (PREFIXED_CATEGORIES.has(prefix)) {
      path = `${prefix}/${slug.slice(dashIndex + 1)}`
    } else {
      path = `core/${slug}`
    }
  }

  return {
    path,
    gradlePath: path.replace(/\//g, ':'),
  }
}
