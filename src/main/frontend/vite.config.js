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
        main: resolve(__dirname, 'src/app/app.main.js'),
        admin: resolve(__dirname, 'src/app/app.admin.js'),
        editor: resolve(__dirname, 'src/app/app.editor.js'),
        style: resolve(__dirname, 'src/app/style.css'),
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
