import tailwindcss from '@tailwindcss/vite';
import { resolve } from 'path';
import { defineConfig } from 'vite';

export default defineConfig({
  plugins: [tailwindcss()],
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src'),
    },
  },
  optimizeDeps: {
    exclude: ['@tailwindcss/oxide', 'lightningcss'],
  },
  build: {
    manifest: true,
    outDir: '../resources/static',
    emptyOutDir: false,
    rollupOptions: {
      input: {
        main: resolve(__dirname, 'src/app.main.js'),
        admin: resolve(__dirname, 'src/app.admin.js'),
        editor: resolve(__dirname, 'src/app.editor.js'),
        style: resolve(__dirname, 'src/style.css'),
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
