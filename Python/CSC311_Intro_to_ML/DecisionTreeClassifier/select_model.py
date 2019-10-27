"""
CSC311 Assignment_1 Q2 (b)
"""
from typing import List
import numpy as np
from sklearn.tree import DecisionTreeClassifier, export_graphviz
from DecisionTreeClassifier.load_data import load_data


def select_model(d_train: np.ndarray, l_train: np.ndarray, d_val: np.ndarray, l_val: np.ndarray,
                 depth_to_test: List[int]):
    """
    Train the DecisionTreeClassifier with the given training data set,d_train, and labels, l_train, using different
    maximum depths in depth_to_test and split criteria (i.e. entropy and gini). Then use the validation data set, d_val,
    and labels, l_val, to compute acccuracies of each model and return the one with the highest accuracy.

    :param d_train: training data set
    :type d_train: np.ndarray
    :param l_train: training labels
    :type l_train: np.ndarray
    :param d_val: validation data set
    :type d_val: np.ndarray
    :param l_val: validation labels
    :type l_val: np.ndarray
    :param depth_to_test: A list of depths to be tested
    :type depth_to_test: List[int]
    :return: the DecisionTreeClassifier with the highest accuracy
    :rtype: DecisionTreeClassifier
    """
    max_accuracy = 0
    best_model = None

    # loop over two split criteria and a list of different maximum depths to test different models
    for split_criteria in ["entropy", "gini"]:
        for max_depth in depth_to_test:
            correct_count = 0
            classifier = DecisionTreeClassifier(criterion=split_criteria, max_depth=max_depth, random_state=1)
            classifier.fit(d_train, l_train)
            l_result = classifier.predict(d_val)
            for i in range(len(l_result)):
                if l_result[i] == l_val[i]:
                    correct_count += 1
            accuracy = correct_count / len(l_result)
            print(f"Split Criteria: {split_criteria}\nMax Depth: {max_depth}\nAccuracy: {100 * accuracy:.1f}%\n")
            if accuracy > max_accuracy:
                max_accuracy = accuracy
                best_model = classifier
    return best_model


if __name__ == "__main__":
    features, *data_set = load_data("clean_fake.txt", "clean_real.txt")
    depth = list(range(10, 15))
    best_model = select_model(data_set[0], data_set[1], data_set[2], data_set[3], depth)
    export_graphviz(best_model, feature_names=features, max_depth=2, out_file="tree.dot")
