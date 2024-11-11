FROM node:18-alpine AS build

WORKDIR /app

COPY package.json /app/

RUN npm install

COPY public/ /app/public
COPY src/ /app/src

RUN npm run build

# Nginx with React Router considerations
FROM nginx:alpine
COPY --from=build /app/build /usr/share/nginx/html

COPY nginx/nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
