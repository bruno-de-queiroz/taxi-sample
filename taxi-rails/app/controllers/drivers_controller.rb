class DriversController < ApplicationController
  wrap_parameters :driver, include: [:name, :carPlate, :driverAvailable, :latitude, :longitude]
  before_action :set_driver, only: [:show, :status, :update_status]

  # GET /drivers
  # GET /drivers.json
  def index
    @drivers = Driver.all
  end

  # GET /drivers/inArea
  # GET /drivers/inArea.json
  def area
    @drivers = Driver.in_area(params[:sw], params[:ne])
  end

  # GET /drivers/1
  def show
  end

  # POST /clients
  # POST /clients.json
  def create
    @driver = Driver.new(driver_params)
    if @driver.save
      render :nothing => true, :status => :ok
    else
      render_error @driver.errors, :precondition_failed
    end
  end

  # GET /drivers/1/status
  def status
  end

  # PUT /drivers/1/status
  def update_status
    if @driver.update_attributes(status_params)
      render :nothing => true, :status => :ok
    else
      render_error @driver.errors, :precondition_failed
    end
  end

  private
  def set_driver
    @driver = Driver.find(params[:id])
  end

  def status_params
    params.require(:driver).permit(:driverAvailable, :latitude, :longitude)
  end

  def driver_params
    params.require(:driver).permit(:name, :carPlate)
  end

  def render_error(errors, status)
    render :template => 'validation.json.jbuilder', :locals => {errors: errors.full_messages}, :status => status, :format => :json
  end
end
