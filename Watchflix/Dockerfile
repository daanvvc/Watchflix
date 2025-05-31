#  Install the dependencies
FROM node:22-alpine as development
COPY package.json package-lock.json ./
RUN npm ci

# Build the application
FROM node:22-alpine As build
WORKDIR /app
ARG VITE_SUPABASE_URL
ARG VITE_SUPABASE_ANON_KEY
ENV VITE_SUPABASE_URL=$VITE_SUPABASE_URL
ENV VITE_SUPABASE_ANON_KEY=$VITE_SUPABASE_ANON_KEY
ENV VITE_HOST=/api
COPY --from=development /node_modules ./node_modules
COPY . .
RUN npm run build

# When running application only include what's needed
FROM node:22-alpine
COPY package*.json ./
COPY --from=development /node_modules ./node_modules
RUN npm prune --production
COPY --from=build app/dist/ ./

# Serve with NGINX, with nginx.conf to send requests to the gateway
FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 3000