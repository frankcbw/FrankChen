"""
CSC311 Assignment_1 Q2 (d)
"""
from collections import Counter
from math import log2
from typing import List
import numpy as np
import pandas as pd
from DecisionTreeClassifier.load_data import load_data


def entropy(data: pd.DataFrame, label: str) -> float:
    """
    Calculate the entropy of data using data[label] as the labels.

    :param data: the dataset
    :type data: pd.DataFrame
    :param label: the column name of labels in data
    :type label: str
    :return: the entropy
    :rtype: float
    """
    count = Counter(data[label])
    n_a = count[1]
    n_b = count[0]
    if n_a == 0 or n_b == 0:
        return 0.0
    return - log2(n_a / (n_a + n_b)) * n_a / (n_a + n_b) - log2(n_b / (n_a + n_b)) * n_b / (n_a + n_b)


def compute_information_gain(features_name: List[str], d_train: np.ndarray, l_train: np.ndarray,
                             split_feature: str, threshold: float) -> float:
    """
    Compute the information gain of spliting at split_feature by threshold. (Rounded up to four decimals)

    :param features_name: A list of feature names
    :type features_name: List[str]
    :param d_train: data of traing sets
    :type d_train: np.ndarray
    :param l_train: labels of training set
    :type l_train: np.ndarray
    :param split_feature: the feature to split at
    :type split_feature: str
    :param threshold: the value to split comparing with
    :type threshold: float
    :return: the information gain
    :rtype: float
    """
    df_train = pd.DataFrame(data=d_train, columns=features_name)
    df_train['label'] = l_train

    root_entropy = entropy(df_train, "label")
    df_left = df_train[df_train[split_feature] <= threshold]
    df_right = df_train[df_train[split_feature] > threshold]
    left_entropy = entropy(df_left, "label")
    right_entropy = entropy(df_right, "label")
    ig = root_entropy - left_entropy * len(df_left)/len(df_train) - right_entropy * len(df_right) / len(df_train)

    return round(ig, 4)


if __name__ == "__main__":
    features, *data_set = load_data("clean_fake.txt", "clean_real.txt")
    print("Information gain of splitting by: trump")
    print(compute_information_gain(features, data_set[0], data_set[1], "trump", 0.5))
    print("Information gain of splitting by: hillary")
    print(compute_information_gain(features, data_set[0], data_set[1], "hillary", 0.5))
    print("Information gain of splitting by: the")
    print(compute_information_gain(features, data_set[0], data_set[1], "the", 0.5))
    print("Information gain of splitting by: tribute")
    print(compute_information_gain(features, data_set[0], data_set[1], "tribute", 0.5))
