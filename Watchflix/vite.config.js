import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  test: {
    globals: true,
    environment: 'jsdom', 
    include: ['src/**/*.test.{js,jsx,ts,tsx}'],
    coverage: {
      provider: 'c8',
      reporter: ['text', 'lcov'],
      reportsDirectory: './coverage',
    },
  },
})
