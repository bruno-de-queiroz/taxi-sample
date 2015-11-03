class CreateDrivers < ActiveRecord::Migration
  def change
    create_table :drivers do |t|
      t.string :name, null: false
      t.string :carPlate, null: false
      t.boolean :driverAvailable, default: false
      t.st_point :location, geographic: true

      t.timestamps null: false
    end
    add_index :drivers, :carPlate, unique: true
    add_index :drivers, :location, using: :gist
  end
end
