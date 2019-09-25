FROM node:10 AS build

COPY generated/package.json generated/package-lock.json /client/generated/
WORKDIR /client/generated
RUN npm install

COPY generated/tsconfig.json /client/generated/
COPY generated/src /client/generated/src
RUN npm run build

COPY package.json package-lock.json /client/
WORKDIR /client
RUN npm install

COPY prettier.config.js tsconfig.json tslint.json /client/
COPY src /client/src
COPY public /client/public
RUN npm run build
WORKDIR /client/build

FROM nginx:1.16-alpine

COPY nginx.conf /etc/nginx/nginx.conf
COPY --from=build /client/build /usr/share/nginx/html
