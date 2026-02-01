.PHONY: help build up down logs status clean restart

help:
	@echo "Prime Neural Labs - Docker Compose Commands"
	@echo ""
	@echo "Available targets:"
	@echo "  make up       - Start backend and frontend containers"
	@echo "  make build    - Build backend and frontend images"
	@echo "  make down     - Stop and remove containers"
	@echo "  make logs     - View logs from all containers"
	@echo "  make status   - Show container status"
	@echo "  make restart  - Restart all containers"
	@echo "  make clean    - Remove containers and volumes"
	@echo ""

build:
	@echo "Building Docker images..."
	cd infra/docker && docker-compose build

up: build
	@echo "Starting containers..."
	cd infra/docker && docker-compose up -d
	@echo "✓ Containers started!"
	@echo ""
	@echo "Services are available at:"
	@echo "  Frontend: http://localhost:3000"
	@echo "  Backend:  http://localhost:8080"

down:
	@echo "Stopping containers..."
	cd infra/docker && docker-compose down
	@echo "✓ Containers stopped!"

logs:
	cd infra/docker && docker-compose logs -f

status:
	cd infra/docker && docker-compose ps

restart:
	@echo "Restarting containers..."
	cd infra/docker && docker-compose restart
	@echo "✓ Containers restarted!"

clean:
	@echo "Cleaning up containers and volumes..."
	cd infra/docker && docker-compose down -v
	@echo "✓ Cleanup complete!"
