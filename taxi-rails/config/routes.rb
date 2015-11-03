Rails.application.routes.draw do

  scope '/drivers', :controller => :drivers, :defaults => {format: :json} do
    get '/inArea' => :area, constraints: lambda { |request|
      regexp = /\A[+\-]?[0-9]{2}\.[0-9]+(%2C|,)[+\-]?[0-9]{2}\.[0-9]+\z/
      sw = request.params['sw']
      ne = request.params['ne']
      !sw.nil? && !ne.nil? && !(regexp =~ sw).nil? && !(regexp =~ ne).nil?
    }, :as => :in_area

    get '/' => :index, :as => :index
    post '/' => :create, :as => :create
    get '/:id' => :show, :as => :driver
    get '/:id/status' => :status, :as => :driver_status
    put '/:id/status' => :update_status, :as => :driver_update_status
  end

end
