class Driver < ActiveRecord::Base
  attr_accessor :latitude, :longitude

  validates :name, presence: true, :on => :create
  validates :carPlate, presence: true, uniqueness: true, format: {with: /\A[a-zA-Z]{3}\-[0-9]{4}\z/}, :on => :create

  validates :driverAvailable, :inclusion => {:in => [true, false]}, :on => :update
  validates :latitude, numericality: {only_integer: false}, :on => :update
  validates :longitude, numericality: {only_integer: false}, :on => :update

  before_update :create_point

  def create_point
    self.location = "POINT(#{longitude} #{latitude})"
  end

  def self.in_area(sw, ne)
    factory = RGeo::ActiveRecord::SpatialFactoryStore.instance.factory(:geo_type => 'point')
    sw = sw.split(",")
    ne = ne.split(",")
    sw_point = factory.point(sw[1].to_f, sw[0].to_f)
    ne_point = factory.point(ne[1].to_f, ne[0].to_f)
    box = RGeo::Cartesian::BoundingBox.create_from_points(sw_point, ne_point).to_geometry
    where("location && ?", box).all
  end

end
