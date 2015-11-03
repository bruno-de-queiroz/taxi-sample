json.ignore_nil!
json.driverId driver.id
json.driverAvailable driver.driverAvailable
json.latitude driver.location.nil? ? nil : driver.location.latitude
json.longitude driver.location.nil? ? nil : driver.location.longitude
hateoas(json, driver)