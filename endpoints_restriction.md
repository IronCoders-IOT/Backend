# API Endpoints Documentation

## ProfilesController

### Create Profile
- **Endpoint**: `POST /api/v1/profiles`
- **Roles**: `ROLE_PROVIDER`, `ROLE_ADMIN`
- **Description**: Only authorized users can create new profiles

### Get Own Profile
- **Endpoint**: `GET /api/v1/profiles/me`
- **Roles**: `ROLE_ADMIN`, `ROLE_PROVIDER`, `ROLE_RESIDENT`
- **Description**: Allows all user types to access their profile information

### Update Profile
- **Endpoint**: `PUT /api/v1/profiles/me/edit`
- **Roles**: `ROLE_ADMIN`, `ROLE_PROVIDER`, `ROLE_RESIDENT`
- **Description**: Users can only modify their own profiles (verified by service layer)

## ProviderController

### Create Provider
- **Endpoint**: `POST /api/v1/providers`
- **Roles**: `ROLE_PROVIDER`, `ROLE_ADMIN`
- **Description**: Ensures proper provider registration

### Update Provider
- **Endpoint**: `PUT /api/v1/providers/edit`
- **Roles**: `ROLE_PROVIDER`, `ROLE_ADMIN`
- **Description**: Providers can only modify their own data (verified by service layer)

### Get Provider Details
- **Endpoint**: `GET /api/v1/providers/{providerId}/detail`
- **Roles**: `ROLE_PROVIDER`, `ROLE_RESIDENT`, `ROLE_ADMIN`
- **Description**: Allows residents to see their provider's information

### Get Own Provider Details
- **Endpoint**: `GET /api/v1/providers/me`
- **Roles**: `ROLE_PROVIDER`, `ROLE_ADMIN`
- **Description**: Specific to provider operations

## ResidentController

### Create Resident
- **Endpoint**: `POST /api/v1/residents`
- **Roles**: `ROLE_PROVIDER`, `ROLE_ADMIN`
- **Description**: Ensures providers can register their residents

### Get Residents by Provider
- **Endpoint**: `GET /api/v1/residents/by-provider/{providerId}`
- **Roles**: `ROLE_PROVIDER`, `ROLE_ADMIN`
- **Description**: Allows providers to manage their residents

### Get Residents for Authenticated Provider
- **Endpoint**: `GET /api/v1/residents`
- **Roles**: `ROLE_PROVIDER`, `ROLE_ADMIN`
- **Description**: Service layer verifies the provider's identity

### Get Own Resident Details
- **Endpoint**: `GET /api/v1/residents/me`
- **Roles**: `ROLE_RESIDENT`
- **Description**: Specific to resident operations

### Update Resident
- **Endpoint**: `PUT /api/v1/residents/me/edit`
- **Roles**: `ROLE_RESIDENT`
- **Description**: Residents can only update their own information (verified by service layer)

## SubscriptionController

### Create Subscription
- **Endpoint**: `POST /api/v1/subscriptions`
- **Roles**: `ROLE_PROVIDER`, `ROLE_ADMIN`
- **Description**: Ensures proper subscription management

### Get Subscriptions by Resident
- **Endpoint**: `GET /api/v1/subscriptions/resident/{id}`
- **Roles**: `ROLE_PROVIDER`, `ROLE_ADMIN`, `ROLE_RESIDENT`
- **Description**: Allows both parties to see subscription details

### Get All Subscriptions
- **Endpoint**: `GET /api/v1/subscriptions`
- **Roles**: `ROLE_PROVIDER`, `ROLE_ADMIN`
- **Description**: Privileged operation

### Update Subscription
- **Endpoint**: `PUT /api/v1/subscriptions/{id}`
- **Roles**: `ROLE_PROVIDER`, `ROLE_ADMIN`
- **Description**: Service layer handles the update logic

## RequestController

### Create Request
- **Endpoint**: `POST /api/v1/requests`
- **Roles**: `ROLE_RESIDENT`
- **Description**: Matches the service layer's check for resident role

### Update Request
- **Endpoint**: `PUT /api/v1/requests/{id}`
- **Roles**: `ROLE_PROVIDER`
- **Description**: Providers can only update their own requests (verified by service layer)

### Get Request by ID
- **Endpoint**: `GET /api/v1/requests/{id}`
- **Roles**: `ROLE_PROVIDER`, `ROLE_RESIDENT`
- **Description**: Allows both parties to see request details

### Get Requests by Resident
- **Endpoint**: `GET /api/v1/requests/resident/{residentId}`
- **Roles**: `ROLE_PROVIDER`, `ROLE_RESIDENT`
- **Description**: Allows residents to see their own requests and providers to see requests from their residents

### Get Requests by Provider
- **Endpoint**: `GET /api/v1/requests/provider/{providerId}`
- **Roles**: `ROLE_PROVIDER`
- **Description**: Specific to provider operations

## WaterRequestController

### Get All Water Requests
- **Endpoint**: `GET /api/v1/water-request`
- **Roles**: `ROLE_PROVIDER`
- **Description**: Provider-specific operation

### Get Water Request by ID
- **Endpoint**: `GET /api/v1/water-request/{id}`
- **Roles**: `ROLE_PROVIDER`, `ROLE_RESIDENT`
- **Description**: Allows both parties to see request details

### Get Water Requests by Resident
- **Endpoint**: `GET /api/v1/water-request/resident/{residentId}`
- **Roles**: `ROLE_PROVIDER`, `ROLE_RESIDENT`
- **Description**: Allows residents to see their own requests and providers to see requests from their residents

### Create Water Request
- **Endpoint**: `POST /api/v1/water-request`
- **Roles**: `ROLE_RESIDENT`
- **Description**: Matches the service layer's check for resident role

### Update Water Request
- **Endpoint**: `PUT /api/v1/water-request/{id}`
- **Roles**: `ROLE_PROVIDER`
- **Description**: Providers can only update their own requests (verified by service layer)

## EventController

### Create Event
- **Endpoint**: `POST /api/v1/events`
- **Roles**: `ROLE_PROVIDER`, `ROLE_ADMIN`
- **Description**: Privileged operation for monitoring

### Get Events by Sensor ID
- **Endpoint**: `GET /api/v1/events/sensor/{id}`
- **Roles**: `ROLE_PROVIDER`, `ROLE_RESIDENT`
- **Description**: Allows both parties to monitor sensor events

## SensorController

### Get Sensor by Resident ID
- **Endpoint**: `GET /api/v1/sensors/resident/{residentId}`
- **Roles**: `ROLE_PROVIDER`, `ROLE_RESIDENT`
- **Description**: Allows residents to monitor their own sensors and providers to monitor their residents' sensors

### Get All Sensors by Resident ID
- **Endpoint**: `GET /api/v1/sensors/resident/{residentId}/all`
- **Roles**: `ROLE_PROVIDER`, `ROLE_RESIDENT`
- **Description**: Allows residents to monitor all their sensors and providers to monitor all their residents' sensors

## General Security
All controllers have class-level `@PreAuthorize("isAuthenticated()")` to ensure that only authenticated users can access any endpoint.