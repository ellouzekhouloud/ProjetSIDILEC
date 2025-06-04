# Étape 1 : Build de l'app Angular
FROM node:20 AS build
WORKDIR /app

# Copie des dépendances
COPY package*.json ./
RUN npm ci --legacy-peer-deps

# Copie du reste du projet Angular
COPY . .

# Build de l'app Angular (assure-toi que "Sidilec" est le nom correct du projet dans angular.json)
RUN npm run build -- --configuration production --project Sidilec

# Étape 2 : Image Nginx pour servir l'app compilée
FROM nginx:alpine

# Copie des fichiers de build Angular vers le dossier de Nginx
COPY --from=build /app/dist/sidilec /usr/share/nginx/html

# Copie de ta config Nginx (si tu as un fichier nginx.conf adapté à Angular)
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
