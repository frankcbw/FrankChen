"""
CSC311_A1: DecisionTreeClassifier
"""
from typing import List, Tuple
import numpy as np
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.model_selection import train_test_split
from sklearn.tree import DecisionTreeClassifier

from DecisionTreeClassifier.compute_information_gain import compute_information_gain


def load_data(fake_d: str, real_d: str) -> Tuple:
    """
    Load both fake data and clean data and split them into traing set, validation set, and testing set using the ratio
    of 0.70: 0.15: 0.15.

    :param fake_d: the filename of fake data
    :type fake_d: str
    :param real_d: the filename of clean data
    :type real_d: str
    :return: the splitted training, validation, and training dataset
    :rtype: Tuple(np.ndarray)
    """

    # open and read the fake news file and real news file
    fake_f = open(fake_d, 'r')
    clean_f = open(real_d, 'r')
    fake_content, real_content = fake_f.readlines(), clean_f.readlines()

    # count the number of fake news and real news
    n_fake, n_real = len(fake_content), len(real_content)

    # make the target label vector
    y = [0] * n_fake + [1] * n_real

    content = fake_content + real_content
    vectorizer = CountVectorizer()
    x = vectorizer.fit_transform(content)
    feature_lst = vectorizer.get_feature_names()
    n_data = n_fake + n_real
    n_feature = len(feature_lst)
    x = x.toarray().reshape(n_data, n_feature)

    # split the data set by 7:3 to separate the training set
    x_train, x_remain, y_train, y_remain = train_test_split(x, y, train_size=0.7, random_state=1)

    # split the remaining data set by 1:1 to separate the validation and tetsing sets
    x_val, x_test, y_val, y_test = train_test_split(x_remain, y_remain, test_size=0.5, random_state=1)

    return feature_lst, x_train, y_train, x_val, y_val, x_test, y_test


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
    print(compute_information_gain(features, data_set[0], data_set[1], "trump", 0.5))
    print(compute_information_gain(features, data_set[0], data_set[1], "donald", 0.5))
    print(compute_information_gain(features, data_set[0], data_set[1], "clinton", 0.5))
    print(compute_information_gain(features, data_set[0], data_set[1], "hillary", 0.5))
    # depth = list(range(10, 15))
    # best_model = select_model(data_set[0], data_set[1], data_set[2], data_set[3], depth)
    # tree.export_graphviz(best_model, feature_names=features, max_depth=2)
