module ApplicationHelper
  def hateoas(json, resource)
    json._links do
      json.self do
        json.href polymorphic_url(resource)
      end
    end
  end
end
