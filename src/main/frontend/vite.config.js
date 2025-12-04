import tailwindcss from '@tailwindcss/vite';
import { resolve } from 'path';
import { defineConfig } from 'vite';

export default defineConfig({
  plugins: [tailwindcss()],
  build: {
    manifest: true,
    outDir: '../resources/static',
    emptyOutDir: false,
    rollupOptions: {
      input: {
        app: resolve(__dirname, 'src/app/app.js'),
      },
      output: {
        entryFileNames: 'builds/[name]-[hash].js',
        chunkFileNames: 'builds/[name]-[hash].js',
        assetFileNames: 'builds/[name]-[hash].[ext]',
      },
    },
  },
  server: {
    port: 5173,
    watch: {
      usePolling: true,
    },
  },
});
