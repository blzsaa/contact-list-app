FROM node:10.16.3-alpine

COPY ${BUILD} build
RUN npm install serve -g

EXPOSE 5000

CMD ["serve", "-s", "/build"]
