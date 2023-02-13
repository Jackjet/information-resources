export function toggleClass (element, className) {
  if (!element || !className) {
    return
  }
  element.className = className
}
