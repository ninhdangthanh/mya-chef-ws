package vn.com.ids.myachef.business.validation.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import vn.com.ids.myachef.business.validation.annotations.NotFalse;

public class NotFalseValidator implements ConstraintValidator<NotFalse, Object> {
    private String[] properties;
    private String[] messages;
    private String[] verifiers;

    @Override
    public void initialize(NotFalse flag) {
        properties = flag.properties();
        messages = flag.messages();
        verifiers = flag.verifiers();
    }

    @Override
    public boolean isValid(Object bean, ConstraintValidatorContext cxt) {
        if (bean == null) {
            return false;
        }

        boolean valid = true;
        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(bean);

        for (int i = 0; i < properties.length; i++) {
            Boolean verified = (Boolean) beanWrapper.getPropertyValue(verifiers[i]);
            valid &= isValidProperty(verified, messages[i], properties[i], cxt);
        }

        return valid;
    }

    boolean isValidProperty(Boolean flag, String message, String property, ConstraintValidatorContext cxt) {
        if (flag == null || flag) {
            return true;
        } else {
            cxt.disableDefaultConstraintViolation();
            cxt.buildConstraintViolationWithTemplate(message).addPropertyNode(property).addConstraintViolation();
            return false;
        }

    }

}

