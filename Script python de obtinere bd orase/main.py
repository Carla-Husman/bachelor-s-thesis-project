import json
import re


def extract_cities_countries():
    file_cities = open("cities.json", encoding="utf8")
    file_dangerous_cities = open("dangerous_cities.txt", encoding="utf8")
    file_dangerous_countries = open("dangerous_countries.txt", encoding="utf8")
    file_name_to_avoid = open("name_to_avoid.txt", encoding="utf8")
    cities_1000_population = open("cities_1000_population.txt", encoding="utf8")

    cities = json.loads(file_cities.read())
    cities_1000_population = cities_1000_population.read().split("\n")
    dangerous_cities = file_dangerous_cities.read().split("\n")
    dangerous_countries = file_dangerous_countries.read().split("\n")
    name_to_avoid = file_name_to_avoid.read().split("\n")

    file_cities.close()
    file_dangerous_cities.close()
    file_dangerous_countries.close()
    file_name_to_avoid.close()

    pattern = re.compile(r'\b(?:' + '|'.join(re.escape(word) for word in name_to_avoid) + r')\b', flags=re.IGNORECASE)

    final_result = open("city_country.txt", encoding="utf8", mode="w")

    for i in range(len(cities)):
        flag = False

        # delete dangerous cities and countries or cities with less than 1000 population
        # or cities to avoid
        if dangerous_countries.__contains__(cities[i]["country_name"]) or \
                dangerous_cities.__contains__(cities[i]["name"] + ", " + cities[i]["country_name"]) or \
                pattern.search(cities[i]["name"]) or \
                not cities_1000_population.__contains__(cities[i]["name"]) or \
                cities[i]["name"].__contains__("/") or \
                cities[i]["name"].__contains__("(") or \
                (cities[i]["name"].__contains__("San José") and cities[i]["country_name"] != "United States"):
            flag = True

        if not flag:
            if cities[i]["name"].__contains__("/"):
                final_result.write(
                    str(cities[i]["name"].split(" / ")[0]) + ", " + str(cities[i]["country_name"]) + "\n")
            elif cities[i]["country_name"].__contains__("("):
                final_result.write(str(cities[i]["name"]) + ", " + str(cities[i]["country_name"].split(" ")[0] + " " +
                                                                       str(cities[i]["country_name"].split(" ")[1])) +
                                   "\n")
            else:
                final_result.write(str(cities[i]["name"]) + ", " + str(cities[i]["country_name"]) + "\n")

    final_result.close()


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    extract_cities_countries()
