import tailwindcss from '@tailwindcss/vite';
import { resolve } from 'path';
import { defineConfig } from 'vite';
import { viteStaticCopy } from 'vite-plugin-static-copy';

export default defineConfig({
  plugins: [
    tailwindcss(),
    viteStaticCopy({
      targets: [
        {
          src: 'node_modules/tinymce/skins/*',
          dest: 'tinymce/skins'
        }
      ]
    })
  ],
  publicDir: false,
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src'),
    },
  },
  build: {
    manifest: false,
    outDir: '../resources/vite',
    emptyOutDir: false,
    rollupOptions: {
      input: {
        'app.main': resolve(__dirname, 'src/app.main.js'),
        'app.admin': resolve(__dirname, 'src/app.admin.js'),
        'app.editor': resolve(__dirname, 'src/app.editor.js'),
        style: resolve(__dirname, 'src/style.css'),
      },
      output: {
        entryFileNames: 'builds/[name].js',
        chunkFileNames: 'builds/[name].js',
        assetFileNames: 'builds/[name].[ext]',
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
