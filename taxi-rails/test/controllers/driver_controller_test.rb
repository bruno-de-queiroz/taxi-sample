require 'test_helper'

class DriverControllerTest < ActionController::TestCase

  def setup
    @controller = DriversController.new
    @latitude = -23.60810717
    @longitude = -46.67500346
  end

  test 'assert inArea routing' do
    point = "#{@latitude},#{@longitude}"
    assert_routing '/drivers/inArea',
                   {:controller => "drivers", :action => "area", :sw => point, :ne => point, :format => :json},
                   {},
                   {:sw => point, :ne => point}
  end

  test 'create with no data' do
    post :create, :driver => {name: "", carPlate: ""}
    assert_response :precondition_failed
  end

  test 'create with an invalid carPlate' do
    post :create, :driver => {name: "Test", carPlate: "asasa"}
    assert_response :precondition_failed
  end

  test 'create' do
    post :create, :driver => {name: "Test", carPlate: "TST-1234"}
    assert_response :success
  end

  test 'get drivers' do
    get :index, :format => :json
    assert_response :success
    drivers = JSON.parse(@response.body)
    assert drivers.count > 0
  end

  test 'get driver' do
    id = last_driver
    assert !id.nil?

    get :show, :id => id, :format => :json
    assert_response :success
  end

  test 'update driver with invalid coordinate' do
    id = last_driver
    assert !id.nil?

    get :update_status, :id => id, :driver => {latitude: "a", longitude: "b", driverAvailable: false}, :format => :json
    assert_response :precondition_failed
  end

  test 'update driver with valid coordinate' do
    id = last_driver
    assert !id.nil?

    get :update_status, :id => id, :driver => {latitude: @latitude-1, longitude: @longitude-1, driverAvailable: true}, :format => :json
    assert_response :success
  end

  test 'list driver with no results' do
    point = "#{@latitude},#{@longitude}"
    get :area, {:sw => point, :ne => point, :format => :json}
    assert_response :success
    drivers = JSON.parse(@response.body)
    assert drivers.count == 0
  end

  test 'list driver with result' do
    id = last_driver
    assert !id.nil?

    get :update_status, :id => id, :driver => {latitude: @latitude, longitude: @longitude, driverAvailable: true}, :format => :json
    assert_response :success

    point = "#{@latitude},#{@longitude}"
    get :area, {:sw => point, :ne => point, :format => :json}
    assert_response :success

    drivers = JSON.parse(@response.body)
    assert drivers.count == 1
    assert drivers[0]["latitude"] == @latitude
    assert drivers[0]["longitude"] == @longitude
    assert drivers[0]["driverAvailable"]
  end

  private
  def last_driver
    get :index, :format => :json
    assert_response :success
    drivers = JSON.parse(@response.body)
    assert drivers.count > 0

    driver = drivers.last
    driver["driverId"]
  end
end
