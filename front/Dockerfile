FROM node:18-alpine AS build

WORKDIR /app

COPY public/ /app/public
COPY src/ /app/src
COPY package.json /app/

RUN npm install

EXPOSE 3000

CMD ["npm", "start"]