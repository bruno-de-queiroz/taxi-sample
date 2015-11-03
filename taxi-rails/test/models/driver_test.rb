require 'test_helper'

class DriverTest < ActiveSupport::TestCase
  # test "the truth" do
  #   assert true
  # end
  fixtures :drivers

  test "create" do
    driver = drivers(:create)
    driver.latitude = -23.60810717
    driver.longitude = -46.67500346
    assert driver.valid?
  end

  test "update" do
    driver = drivers(:update)
    driver.latitude = -23.60810717
    driver.longitude = -46.67500346
    assert driver.valid?, "Driver not valid"
    assert driver.save
    assert !driver.location.nil?, "Driver location is nil"
  end
end
