package pages.data.grades.interfaces;


import pages.data.grades.components.PublishGradesModal;

public interface IPublishGradesRow {
    String getLastPublishedInfo();

    PublishGradesModal clickPublish();
}
