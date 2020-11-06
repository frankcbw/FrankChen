"""
CSC311 Assignment_1 Q2 main
"""
from sklearn.tree import export_graphviz
from DecisionTreeClassifier.load_data import load_data
from DecisionTreeClassifier.select_model import select_model
from DecisionTreeClassifier.compute_information_gain import compute_information_gain

if __name__ == "__main__":
    features, *data_set = load_data("clean_fake.txt", "clean_real.txt")
    depth = list(range(10, 15))
    best_model = select_model(data_set[0], data_set[1], data_set[2], data_set[3], depth)
    export_graphviz(best_model, feature_names=features, max_depth=2, out_file="tree.dot")
    print(compute_information_gain(features, data_set[0], data_set[1], "trump", 0.5))
    print(compute_information_gain(features, data_set[0], data_set[1], "donald", 0.5))
    print(compute_information_gain(features, data_set[0], data_set[1], "clinton", 0.5))
    print(compute_information_gain(features, data_set[0], data_set[1], "hillary", 0.5))
