/// <reference types="vitest/globals" />
import { expect, afterEach } from 'vitest'
import { cleanup } from '@testing-library/react'
import * as matchers from '@testing-library/jest-dom/matchers'
import { vi } from 'vitest'

expect.extend(matchers)

// Mock fetch globally
const globalAny: any = global
globalAny.fetch = vi.fn()

afterEach(() => {
  cleanup()
  vi.clearAllMocks()
})
